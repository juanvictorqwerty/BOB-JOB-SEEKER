'use client';

interface UserData {
    username: string;
    email: string;
}

interface ProfileCardProps {
    user: UserData;
}

const COLORS = [
    '#FF6B6B', '#4ECDC4', '#45B7D1', '#FFA07A',
    '#98D8C8', '#F7DC6F', '#BB8FCE', '#85C1E2',
    '#F8B88B', '#A8E6CF', '#FFD3B6', '#FFAAA5'
];

function getRandomColor(username: string): string {
    const hash = username.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
    return COLORS[hash % COLORS.length];
}

export default function ProfileCard({ user }: ProfileCardProps) {
    const firstLetter = user.username.charAt(0).toUpperCase();
    const bgColor = getRandomColor(user.username);

    return (
        <div className="md:col-span-1">
            <div className="bg-gray-50 rounded-lg p-8 sticky top-8">
                {/* Avatar */}
                <div className="flex justify-center mb-6">
                    <div
                        className="w-24 h-24 rounded-full flex items-center justify-center text-white text-4xl font-bold shadow-lg"
                        style={{ backgroundColor: bgColor }}
                    >
                        {firstLetter}
                    </div>
                </div>

                {/* Username */}
                <h2 className="text-center text-xl font-semibold text-gray-900 mb-2">
                    {user.username}
                </h2>

                {/* Email */}
                <p className="text-center text-sm text-gray-600 break-all">
                    {user.email}
                </p>

                {/* Divider */}
                <div className="h-px bg-gray-200 my-6" />

                {/* Status */}
                <div className="flex items-center justify-center space-x-2">
                    <div className="w-2 h-2 bg-green-500 rounded-full" />
                    <span className="text-sm text-gray-700">Active</span>
                </div>
            </div>
        </div>
    );
}