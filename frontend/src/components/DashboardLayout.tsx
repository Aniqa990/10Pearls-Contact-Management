import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';
import { Button } from '@/components/ui/button';
import { Menu, X, LogOut, User, Home } from 'lucide-react';

interface DashboardLayoutProps {
  children: React.ReactNode;
}

export const DashboardLayout: React.FC<DashboardLayoutProps> = ({ children }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();
  const [sidebarOpen, setSidebarOpen] = React.useState(false);

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  const isActive = (path: string) => location.pathname === path;

  return (
    <div className="flex h-screen bg-gray-100">
      {/* sidebar */}
      <aside
        className={`fixed md:static z-40 w-64 bg-gray-900 text-white h-screen overflow-y-auto transition-transform duration-300 ${
          sidebarOpen ? 'translate-x-0' : '-translate-x-full md:translate-x-0'
        }`}
      >
        <div className="flex flex-col h-full">
          <div className="px-6 py-8 border-b border-gray-800">
            <h1 className="text-2xl font-bold">Contact Manager</h1>
            <p className="text-gray-400 text-sm mt-1">Manage Your Contacts</p>
          </div>

          <nav className="flex-1 px-4 py-8 space-y-2">
            <Button
              onClick={() => {
                navigate('/dashboard');
                setSidebarOpen(false);
              }}
              variant={isActive('/dashboard') ? 'default' : 'ghost'}
              className={`w-full justify-start ${
                isActive('/dashboard')
                  ? 'bg-blue-600 hover:bg-blue-700'
                  : 'text-gray-300 hover:bg-gray-800 hover:text-white'
              }`}
            >
              <Home className="h-5 w-5 mr-3" />
              Contacts
            </Button>

            <Button
              onClick={() => {
                navigate('/profile');
                setSidebarOpen(false);
              }}
              variant={isActive('/profile') ? 'default' : 'ghost'}
              className={`w-full justify-start ${
                isActive('/profile')
                  ? 'bg-blue-600 hover:bg-blue-700'
                  : 'text-gray-300 hover:bg-gray-800 hover:text-white'
              }`}
            >
              <User className="h-5 w-5 mr-3" />
              Profile
            </Button>
          </nav>

          {/* user info and Logout */}
          <div className="px-4 py-6 border-t border-gray-800">
            <div className="mb-4">
              <p className="text-gray-400 text-xs uppercase tracking-wider mb-2">
                Logged in as
              </p>
              <p className="text-white font-medium">
                {user?.firstName} {user?.lastName}
              </p>
              <p className="text-gray-400 text-sm">{user?.email}</p>
            </div>
            <Button
              onClick={handleLogout}
              variant="destructive"
              className="w-full"
            >
              <LogOut className="h-4 w-4 mr-2" />
              Logout
            </Button>
          </div>
        </div>
      </aside>

      <div className="flex-1 flex flex-col overflow-hidden">
        <header className="bg-white border-b border-gray-200 px-4 py-4 flex items-center justify-between md:justify-end">
          <button
            onClick={() => setSidebarOpen(!sidebarOpen)}
            className="md:hidden p-2 hover:bg-gray-100 rounded-lg"
          >
            {sidebarOpen ? (
              <X className="h-6 w-6" />
            ) : (
              <Menu className="h-6 w-6" />
            )}
          </button>
          <div className="hidden md:flex items-center gap-4">
            <span className="text-gray-600">
              Welcome, {user?.firstName}!
            </span>
          </div>
        </header>

        <main className="flex-1 overflow-y-auto">
          {children}
        </main>
      </div>

      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-30 md:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}
    </div>
  );
};
