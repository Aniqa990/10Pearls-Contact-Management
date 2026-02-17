import React, { useState, useEffect } from 'react';
import { useAuth } from '@/context/AuthContext';
import { contactService } from '@/services/contactService';
import type { Contact, CreateContactRequest, PaginatedResponse } from '@/types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { Plus, Edit2, Trash2, Search, ChevronLeft, ChevronRight, AlertCircle, CheckCircle } from 'lucide-react';
import { ContactFormModal } from '../components/ContactFormModal';
import { DeleteConfirmationModal } from '../components/DeleteConfirmationModal';

export const ContactManagementPage: React.FC = () => {
  const { user } = useAuth();
  const [contacts, setContacts] = useState<Contact[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [selectedContact, setSelectedContact] = useState<Contact | null>(null);

  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const userId = user?.id || '';

  const loadContacts = async (page: number = 0, search: string = '') => {
    if (!userId) return;

    try {
      setLoading(true);
      setError(null);
      let response: PaginatedResponse<Contact>;

      if (search) {
        response = await contactService.searchContacts(userId, search, page, pageSize);
      } else {
        response = await contactService.getAllContacts(userId, page, pageSize);
      }

      setContacts(response.content);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
      setCurrentPage(response.number);
      
      console.log('API Response:', response);
      console.log('Contacts received:', response.content);
      if (response.content.length > 0) {
        console.log('First contact:', response.content[0]);
        console.log('Email addresses:', response.content[0].emailAddresses);
        console.log('Phone numbers:', response.content[0].phoneNumbers);
      }
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to load contacts';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadContacts(currentPage, searchQuery);
  }, [userId]);

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    const query = e.target.value;
    setSearchQuery(query);
    setCurrentPage(0);
    loadContacts(0, query);
  };

  const handleCreateContact = async (data: CreateContactRequest) => {
    if (!userId) return;

    try {
      setError(null);
      await contactService.createContact(userId, data);
      setShowCreateModal(false);
      setSuccessMessage('Contact created successfully');
      await loadContacts(currentPage, searchQuery);
      setTimeout(() => setSuccessMessage(null), 3000);
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to create contact';
      setError(message);
    }
  };

  const handleUpdateContact = async (data: CreateContactRequest) => {
    if (!userId || !selectedContact) return;

    try {
      setError(null);
      await contactService.updateContact(userId, selectedContact.id, data);
      setShowEditModal(false);
      setSelectedContact(null);
      setSuccessMessage('Contact updated successfully');
      await loadContacts(currentPage, searchQuery);
      setTimeout(() => setSuccessMessage(null), 3000);
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to update contact';
      setError(message);
    }
  };

  const handleDeleteContact = async () => {
    if (!userId || !selectedContact) return;

    try {
      setError(null);
      await contactService.deleteContact(userId, selectedContact.id);
      setShowDeleteModal(false);
      setSelectedContact(null);
      setSuccessMessage('Contact deleted successfully');
      await loadContacts(currentPage, searchQuery);
      setTimeout(() => setSuccessMessage(null), 3000);
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Failed to delete contact';
      setError(message);
    }
  };

  const handleEditClick = (contact: Contact) => {
    setSelectedContact(contact);
    setShowEditModal(true);
  };

  const handleDeleteClick = (contact: Contact) => {
    setSelectedContact(contact);
    setShowDeleteModal(true);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 to-purple-100 p-4 md:p-8">
      <div className="max-w-7xl mx-auto space-y-6">
        <div className="bg-gradient-to-r from-purple-600 to-purple-500 p-6 rounded-lg text-white flex items-center justify-between">
          <div>
            <h1 className="text-3xl md:text-4xl font-bold">Contacts</h1>
            <p className="mt-1 text-purple-200">Manage all your contacts in one place</p>
          </div>
          
        </div>

        {error && (
          <Alert variant="destructive">
            <AlertCircle className="h-4 w-4" />
            <AlertTitle>Error</AlertTitle>
            <AlertDescription>{error}</AlertDescription>
          </Alert>
        )}

        {successMessage && (
          <Alert>
            <CheckCircle className="h-4 w-4" />
            <AlertTitle>Success</AlertTitle>
            <AlertDescription>{successMessage}</AlertDescription>
          </Alert>
        )}

        {/*main content */}
        <Card>
          <CardHeader>
            <CardTitle>Your Contacts</CardTitle>
            <CardDescription>View, search, and manage all your contacts</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">

            <div className="flex flex-col md:flex-row gap-4">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-2.5 text-gray-400 h-5 w-5" />
                <Input
                  type="text"
                  placeholder="Search by first name or last name..."
                  value={searchQuery}
                  onChange={handleSearch}
                  className="pl-10"
                />
              </div>
              <Button
                onClick={() => setShowCreateModal(true)}
                className="whitespace-nowrap"
              >
                <Plus className="h-5 w-5 mr-2" />
                Add Contact
              </Button>
            </div>


            {loading ? (
              <div className="text-center py-12">
                <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
                <p className="mt-4 text-gray-600">Loading contacts...</p>
              </div>
            ) : contacts.length === 0 ? (
              <div className="text-center py-12">
                <p className="text-gray-600 text-lg">
                  {searchQuery ? 'No contacts found matching your search' : 'No contacts yet. Create your first contact!'}
                </p>
              </div>
            ) : (
              <>

                <div className="border rounded-lg overflow-hidden">
                  <Table>
                    <TableHeader>
                      <TableRow>
                        <TableHead>Name</TableHead>
                        <TableHead>Email</TableHead>
                        <TableHead>Phone</TableHead>
                        <TableHead className="text-right w-20">Actions</TableHead>
                      </TableRow>
                    </TableHeader>
                    <TableBody>
                      {contacts.map((contact) => (
                        <TableRow key={contact.id}>
                          <TableCell className="font-medium">
                            <div className="flex items-center gap-3">
                              <div className="h-10 w-10 rounded-full bg-purple-300 text-white font-semibold flex items-center justify-center">
                                {`${(contact.firstName?.[0] || '').toUpperCase()}${(contact.lastName?.[0] || '').toUpperCase()}`}
                              </div>
                              <div className="truncate">
                                <div className="text-sm font-medium text-gray-900 truncate">{contact.firstName} {contact.lastName}</div>
                                {contact.title && <div className="text-xs text-gray-500 truncate">{contact.title}</div>}
                              </div>
                            </div>
                          </TableCell>
                          <TableCell>
                            {contact.emailAddresses && contact.emailAddresses.length > 0 ? (
                              <div className="flex flex-col gap-1">
                                {contact.emailAddresses.map((e, i) => (
                                  <div key={i} className="flex items-center gap-2">
                                    <span className="text-xs font-medium truncate text-gray-700">{e.email}</span>
                                    <span className="px-1.5 py-0.5 text-xs font-semibold rounded-full bg-purple-100 text-purple-700 whitespace-nowrap">
                                      {e.type}
                                    </span>
                                  </div>
                                ))}
                              </div>
                            ) : (
                              <span className="text-sm text-gray-400">N/A</span>
                            )}
                          </TableCell>
                          <TableCell>
                            {contact.phoneNumbers && contact.phoneNumbers.length > 0 ? (
                              <div className="flex flex-col gap-1">
                                {contact.phoneNumbers.map((p, i) => (
                                  <div key={i} className="flex items-center gap-2">
                                    <span className="text-xs font-medium truncate text-gray-700">{p.number}</span>
                                    <span className="px-1.5 py-0.5 text-xs font-semibold rounded-full bg-purple-100 text-purple-700 whitespace-nowrap">
                                      {p.type}
                                    </span>
                                  </div>
                                ))}
                              </div>
                            ) : (
                              <span className="text-sm text-gray-400">N/A</span>
                            )}
                          </TableCell>
                          <TableCell className="text-right">
                            <div className="flex justify-end gap-2">
                              <Button
                                variant="outline"
                                size="sm"
                                onClick={() => handleEditClick(contact)}
                              >
                                <Edit2 className="h-4 w-4" />
                              </Button>
                              <Button
                                variant="outline"
                                size="sm"
                                onClick={() => handleDeleteClick(contact)}
                              >
                                <Trash2 className="h-4 w-4" />
                              </Button>
                            </div>
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </div>


                <div className="flex items-center justify-between pt-4">
                  <div className="text-sm text-gray-600">
                    Showing {contacts.length === 0 ? 0 : currentPage * pageSize + 1} to{' '}
                    {Math.min((currentPage + 1) * pageSize, totalElements)} of {totalElements}
                  </div>
                  <div className="flex gap-2 items-center">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => {
                        const newPage = currentPage - 1;
                        setCurrentPage(newPage);
                        loadContacts(newPage, searchQuery);
                      }}
                      disabled={currentPage === 0}
                    >
                      <ChevronLeft className="h-4 w-4" />
                    </Button>
                    <span className="px-4 py-2 text-sm font-medium">
                      Page {currentPage + 1} of {totalPages || 1}
                    </span>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => {
                        const newPage = currentPage + 1;
                        setCurrentPage(newPage);
                        loadContacts(newPage, searchQuery);
                      }}
                      disabled={currentPage >= totalPages - 1 || totalPages === 0}
                    >
                      <ChevronRight className="h-4 w-4" />
                    </Button>
                  </div>
                </div>
              </>
            )}
          </CardContent>
        </Card>
      </div>

      <ContactFormModal
        key={showCreateModal ? 'create-open' : 'create-closed'}
        title="Create New Contact"
        open={showCreateModal}
        onSave={handleCreateContact}
        onOpenChange={setShowCreateModal}
      />

      {selectedContact && (
        <ContactFormModal
          title="Edit Contact"
          open={showEditModal}
          onSave={handleUpdateContact}
          onOpenChange={setShowEditModal}
          initialData={selectedContact}
        />
      )}

      {selectedContact && (
        <DeleteConfirmationModal
          title="Delete Contact"
          message={`Are you sure you want to delete ${selectedContact.firstName} ${selectedContact.lastName}? This action cannot be undone.`}
          onConfirm={handleDeleteContact}
          onCancel={() => {
            setShowDeleteModal(false);
            setSelectedContact(null);
          }}
          open={showDeleteModal}
          onOpenChange={setShowDeleteModal}
        />
      )}
    </div>
  );
};
