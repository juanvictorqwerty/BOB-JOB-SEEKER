'use client';

interface RankBadgeProps {
    rank: number;
}

export default function RankBadge({ rank }: RankBadgeProps) {
    const isAdmin = rank === 0;
    const title = isAdmin ? 'Super Admin' : 'Regular User';
    const bgColor = isAdmin ? 'bg-purple-50' : 'bg-blue-50';
    const borderColor = isAdmin ? 'border-purple-200' : 'border-blue-200';
    const textColor = isAdmin ? 'text-purple-900' : 'text-blue-900';
    const badgeColor = isAdmin ? 'bg-purple-600' : 'bg-blue-600';

    return (
        <div className={`${bgColor} border ${borderColor} rounded-lg p-6`}>
            <div className="flex items-center space-x-4">
                <div className={`${badgeColor} text-white px-4 py-2 rounded-full font-medium text-sm`}>
                    Rank {rank}
                </div>
                <div>
                    <h3 className={`${textColor} font-semibold text-lg`}>
                        {title}
                    </h3>
                    <p className="text-gray-600 text-sm mt-1">
                        {isAdmin ? 'Full system access and control' : 'Standard user permissions'}
                    </p>
                </div>
            </div>
        </div>
    );
}