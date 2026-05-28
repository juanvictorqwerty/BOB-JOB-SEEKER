'use client';

interface UserData {
    email: string;
    username: string;
    createdAt: string;
    updatedAt: string;
}

interface InfoGridProps {
    user: UserData;
}

function formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

export default function InfoGrid({ user }: InfoGridProps) {
    const infoItems = [
        {
            label: 'Email Address',
            value: user.email,
            icon: '✉'
        },
        {
            label: 'Username',
            value: user.username,
            icon: '👤'
        },
        {
            label: 'Member Since',
            value: formatDate(user.createdAt),
            icon: '📅'
        },
        {
            label: 'Last Updated',
            value: formatDate(user.updatedAt),
            icon: '🔄'
        }
    ];

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            {infoItems.map((item, index) => (
                <div
                    key={index}
                    className="border border-gray-200 rounded-lg p-4 hover:bg-gray-50 transition-colors"
                >
                    <div className="flex items-start space-x-3">
                        <span className="text-xl leading-none mt-0.5">{item.icon}</span>
                        <div className="flex-1 min-w-0">
                            <p className="text-xs font-medium text-gray-500 uppercase tracking-wide mb-1">
                                {item.label}
                            </p>
                            <p className="text-sm text-gray-900 break-all">
                                {item.value}
                            </p>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}