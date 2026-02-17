import React, { useEffect, useState } from 'react';
import type { Contact, CreateContactRequest } from '@/types';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { X, Plus } from 'lucide-react';

interface ContactFormModalProps {
  title: string;
  open: boolean;
  onSave: (data: CreateContactRequest) => Promise<void>;
  onOpenChange: (open: boolean) => void;
  initialData?: Contact;
}

export const ContactFormModal: React.FC<ContactFormModalProps> = ({
  title,
  open,
  onSave,
  onOpenChange,
  initialData,
}) => {
  const [loading, setLoading] = useState(false);
  const [formErrors, setFormErrors] = useState<{ firstName?: string; lastName?: string }>({});
  const [formData, setFormData] = useState<CreateContactRequest>({
    firstName: initialData?.firstName || '',
    lastName: initialData?.lastName || '',
    emails: (initialData?.emailAddresses || []).map(e => ({ email: e.email, type: e.type })),
    phones: (initialData?.phoneNumbers || []).map(p => ({ number: p.number, type: p.type })),
    title: initialData?.title || '',
  });

  useEffect(() => {
    if (initialData) {
      setFormData({
        firstName: initialData.firstName,
        lastName: initialData.lastName,
        emails: (initialData.emailAddresses || []).map(e => ({ email: e.email, type: e.type })),
        phones: (initialData.phoneNumbers || []).map(p => ({ number: p.number, type: p.type })),
        title: initialData.title || '',
      });
    } else if (open) {
      setFormData({
        firstName: '',
        lastName: '',
        emails: [],
        phones: [],
        title: '',
      });
      setFormErrors({});
    }
  }, [initialData, open]);

  const validateForm = () => {
    const errors: typeof formErrors = {};
    if (!formData.firstName.trim()) errors.firstName = 'First name is required';
    if (!formData.lastName.trim()) errors.lastName = 'Last name is required';
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleAddEmail = () => {
    setFormData({
      ...formData,
      emails: [...(formData.emails || []), { email: '', type: 'PERSONAL' }],
    });
  };

  const handleRemoveEmail = (index: number) => {
    setFormData({
      ...formData,
      emails: (formData.emails || []).filter((_, i) => i !== index),
    });
  };

  const handleEmailChange = (index: number, field: 'email' | 'type', value: string) => {
    const newEmails = [...(formData.emails || [])];
    newEmails[index] = { ...newEmails[index], [field]: value };
    setFormData({ ...formData, emails: newEmails });
  };

  const handleAddPhone = () => {
    setFormData({
      ...formData,
      phones: [...(formData.phones || []), { number: '', type: 'WORK' }],
    });
  };

  const handleRemovePhone = (index: number) => {
    setFormData({
      ...formData,
      phones: (formData.phones || []).filter((_, i) => i !== index),
    });
  };

  const handlePhoneChange = (index: number, field: 'number' | 'type', value: string) => {
    const newPhones = [...(formData.phones || [])];
    newPhones[index] = { ...newPhones[index], [field]: value };
    setFormData({ ...formData, phones: newPhones });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validateForm()) return;

    try {
      setLoading(true);
      await onSave(formData);
    } catch (err) {
      console.error('Error saving contact:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>{title}</DialogTitle>
          <DialogDescription>
            {initialData ? 'Update contact information' : 'Create a new contact'}
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Name Section */}
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="firstName">First Name</Label>
              <Input
                id="firstName"
                type="text"
                placeholder="First name"
                value={formData.firstName}
                onChange={(e) =>
                  setFormData({ ...formData, firstName: e.target.value })
                }
              />
              {formErrors.firstName && (
                <p className="text-sm text-destructive">{formErrors.firstName}</p>
              )}
            </div>
            <div className="space-y-2">
              <Label htmlFor="lastName">Last Name</Label>
              <Input
                id="lastName"
                type="text"
                placeholder="Last name"
                value={formData.lastName}
                onChange={(e) =>
                  setFormData({ ...formData, lastName: e.target.value })
                }
              />
              {formErrors.lastName && (
                <p className="text-sm text-destructive">{formErrors.lastName}</p>
              )}
            </div>
          </div>

          {/* Title Section */}
          <div className="space-y-2">
            <Label htmlFor="title">Title (Optional)</Label>
            <Input
              id="title"
              type="text"
              placeholder="e.g. Senior Manager, Developer"
              value={formData.title || ''}
              onChange={(e) =>
                setFormData({ ...formData, title: e.target.value })
              }
            />
          </div>

          {/* Email Section */}
          <div>
            <div className="flex justify-between items-center mb-3">
              <Label>Emails</Label>
              <button
                type="button"
                onClick={handleAddEmail}
                className="text-primary hover:underline flex items-center gap-1 text-sm"
              >
                <Plus className="h-4 w-4" />
                Add Email
              </button>
            </div>
            <div className="space-y-2">
              {(formData.emails || []).map((email, index) => (
                <div key={index} className="flex gap-2">
                  <Input
                    type="email"
                    placeholder="Email address"
                    value={email.email}
                    onChange={(e) => handleEmailChange(index, 'email', e.target.value)}
                    className="flex-1"
                  />
                  <Select
                    value={email.type}
                    onValueChange={(value) => handleEmailChange(index, 'type', value)}
                  >
                    <SelectTrigger className="w-32">
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectGroup>
                        <SelectItem value="PERSONAL">Personal</SelectItem>
                        <SelectItem value="WORK">Work</SelectItem>
                        <SelectItem value="OTHER">Other</SelectItem>
                      </SelectGroup>
                    </SelectContent>
                  </Select>
                  <button
                    type="button"
                    onClick={() => handleRemoveEmail(index)}
                    className="text-destructive hover:text-destructive/80"
                  >
                    <X className="h-5 w-5" />
                  </button>
                </div>
              ))}
            </div>
          </div>

          {/* Phone Section */}
          <div>
            <div className="flex justify-between items-center mb-3">
              <Label>Phone Numbers</Label>
              <button
                type="button"
                onClick={handleAddPhone}
                className="text-primary hover:underline flex items-center gap-1 text-sm"
              >
                <Plus className="h-4 w-4" />
                Add Phone
              </button>
            </div>
            <div className="space-y-2">
              {(formData.phones || []).map((phone, index) => (
                <div key={index} className="flex gap-2">
                  <Input
                    type="tel"
                    placeholder="Phone number"
                    value={phone.number}
                    onChange={(e) => handlePhoneChange(index, 'number', e.target.value)}
                    className="flex-1"
                  />
                  <Select
                    value={phone.type}
                    onValueChange={(value) => handlePhoneChange(index, 'type', value)}
                  >
                    <SelectTrigger className="w-32">
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectGroup>
                        <SelectItem value="WORK">Work</SelectItem>
                        <SelectItem value="HOME">Home</SelectItem>
                        <SelectItem value="PERSONAL">Personal</SelectItem>
                        <SelectItem value="OTHER">Other</SelectItem>
                      </SelectGroup>
                    </SelectContent>
                  </Select>
                  <button
                    type="button"
                    onClick={() => handleRemovePhone(index)}
                    className="text-destructive hover:text-destructive/80"
                  >
                    <X className="h-5 w-5" />
                  </button>
                </div>
              ))}
            </div>
          </div>

          {/* Action Buttons */}
          <DialogFooter className="gap-3">
            <Button
              type="button"
              variant="outline"
              onClick={() => onOpenChange(false)}
              disabled={loading}
            >
              Cancel
            </Button>
            <Button type="submit" disabled={loading}>
              {loading ? 'Saving...' : 'Save Contact'}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};
