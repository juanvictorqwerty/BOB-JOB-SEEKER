'use client';

import { useEffect, useState } from 'react';
import ProfileCard from '@/components/Account/ProfileCard';
import InfoGrid from '@/components/Account/Infogrid';
import RankBadge from '@/components/Account/RankUser';
import LoadingSpinner from '@/components/Account/LoadingSpinner';

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

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE}/users/me`);
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
                    </div>
                </div>
            </div>
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