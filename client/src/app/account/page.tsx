'use client';

import { useEffect, useState } from 'react';
import ProfileCard from '@/components/Account/ProfileCard';
import InfoGrid from '@/components/Account/Infogrid';
import RankBadge from '@/components/Account/RankUser';
import LoadingSpinner from '@/components/Account/LoadingSpinner';
import UpdateEmailModal from '@/components/Account/UpdateEmailModal';
import UpdatePasswordModal from '@/components/Account/UpdatePasswordModal';

interface UserData {
    success: boolean;
    email: string;
    username: string;
    userRank: number;
    createdAt: string;
    updatedAt: string;
}

export default function AccountPage() {
    const [user, setUser] = useState<UserData | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    
    // Modal states
    const [isEmailModalOpen, setIsEmailModalOpen] = useState(false);
    const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE}/users/me`, {
                    credentials: 'include'
                });
                if (!response.ok) {
                    throw new Error('Failed to fetch user data');
                }
                const data: UserData = await response.json();
                setUser(data);
            } catch (err) {
                setError(err instanceof Error ? err.message : 'An error occurred');
            } finally {
                setLoading(false);
            }
        };

        fetchUser();
    }, []);

    if (loading) return <LoadingSpinner />;
    if (error) return <ErrorState message={error} />;
    if (!user) return <ErrorState message="No user data available" />;

    return (
        <main className="min-h-screen bg-white">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* Profile Section */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-12">
                    <ProfileCard user={user} />

                    {/* Right Column */}
                    <div className="md:col-span-2 space-y-6">
                        {/* Rank Badge */}
                        <RankBadge rank={user.userRank} />

                        {/* Info Grid */}
                        <InfoGrid user={user} />

                        {/* Actions */}
                        <div className="flex flex-col sm:flex-row gap-4 mt-8">
                            <button 
                                className="px-6 py-2.5 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors"
                                onClick={() => setIsEmailModalOpen(true)}
                            >
                                Update Email
                            </button>
                            <button 
                                className="px-6 py-2.5 bg-gray-100 text-gray-700 font-medium rounded-lg border border-gray-300 hover:bg-gray-200 transition-colors"
                                onClick={() => setIsPasswordModalOpen(true)}
                            >
                                Change Password
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            {/* Modals */}
            <UpdateEmailModal 
                isOpen={isEmailModalOpen} 
                onClose={() => setIsEmailModalOpen(false)} 
            />
            <UpdatePasswordModal 
                isOpen={isPasswordModalOpen} 
                onClose={() => setIsPasswordModalOpen(false)} 
            />
        </main>
    );
}

function ErrorState({ message }: { message: string }) {
    return (
        <main className="min-h-screen bg-white">
            <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <div className="p-6 bg-red-50 border border-red-200 rounded-lg">
                    <p className="text-red-800 font-medium">{message}</p>
                </div>
            </div>
        </main>
    );
}