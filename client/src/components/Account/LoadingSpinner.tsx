'use client';

export default function LoadingSpinner() {
    return (
        <main className="min-h-screen bg-white">
            <div className="border-b border-gray-200">
                <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
                    <h1 className="text-3xl font-light tracking-tight text-gray-900">
                        Dashboard
                    </h1>
                </div>
            </div>
            <div className="flex items-center justify-center min-h-96">
                <div className="flex flex-col items-center space-y-4">
                    <div className="relative w-12 h-12">
                        <div className="absolute inset-0 bg-linear-to-r from-gray-200 to-gray-300 rounded-full animate-spin" />
                        <div className="absolute inset-1 bg-white rounded-full" />
                    </div>
                    <p className="text-gray-600 text-sm">Loading your profile...</p>
                </div>
            </div>
        </main>
    );
}