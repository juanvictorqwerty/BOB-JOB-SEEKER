'use client';

import { useState } from 'react';

interface UpdateEmailModalProps {
    isOpen: boolean;
    onClose: () => void;
}

export default function UpdateEmailModal({ isOpen, onClose }: UpdateEmailModalProps) {
    const [newEmail, setNewEmail] = useState('');
    const [currentPassword, setCurrentPassword] = useState('');
    const [status, setStatus] = useState<{ type: 'idle' | 'loading' | 'success' | 'error', message?: string }>({ type: 'idle' });

    if (!isOpen) return null;

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setStatus({ type: 'loading' });

        try {
            const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE}/auth/change-email`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify({
                    newEmail,
                    currentPassword
                })
            });

            const data = await response.text();

            if (response.ok && data.includes('successfully')) {
                setStatus({ type: 'success', message: data });
                setTimeout(() => {
                    onClose();
                    window.location.reload(); // Refresh to see updated email
                }, 2000);
            } else {
                setStatus({ type: 'error', message: data || 'Failed to update email' });
            }
        } catch (err) {
            setStatus({ type: 'error', message: 'Network error occurred' });
        }
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
            <div className="bg-white rounded-xl shadow-xl w-full max-w-md p-6 relative">
                <button 
                    onClick={onClose}
                    className="absolute top-4 right-4 text-gray-400 hover:text-gray-600"
                >
                    ✕
                </button>
                
                <h2 className="text-xl font-semibold text-gray-900 mb-6">Update Email</h2>

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">New Email Address</label>
                        <input
                            type="email"
                            required
                            value={newEmail}
                            onChange={(e) => setNewEmail(e.target.value)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-shadow text-gray-900"
                            placeholder="new@example.com"
                        />
                    </div>
                    
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Current Password</label>
                        <input
                            type="password"
                            required
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-shadow text-gray-900"
                            placeholder="Verify it's you"
                        />
                    </div>

                    {status.message && (
                        <div className={`p-3 rounded-lg text-sm ${status.type === 'error' ? 'bg-red-50 text-red-700' : status.type === 'success' ? 'bg-green-50 text-green-700' : ''}`}>
                            {status.message}
                        </div>
                    )}

                    <div className="mt-6 flex justify-end space-x-3">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-4 py-2 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={status.type === 'loading'}
                            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
                        >
                            {status.type === 'loading' ? 'Updating...' : 'Save Changes'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}
