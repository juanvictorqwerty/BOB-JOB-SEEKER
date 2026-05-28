"use client";

import { useEffect, useState } from "react";
import { PostStatusToggle } from "@/components/PostStatusToggle";

interface MarketPlacePost {
    id: string;
    title: string;
    location: string; // Stored as a raw JSON string from backend
    description: string; // Stored as a raw JSON string from backend
    isOpen: boolean;
    createdAt: string;
}

export default function UserMarketplacePage() {
    const [posts, setPosts] = useState<MarketPlacePost[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        // We no longer need userId in the URL. 
        // The backend reads the HTTP-Only cookie automatically.
        fetch(`${process.env.NEXT_PUBLIC_API_BASE}/marketplace/user/myposts`, {
            method: "GET",
            // CRITICAL: Tells the browser to include cookies with this cross-origin request
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            }
        })
            .then((res) => {
                if (!res.ok) throw new Error("Failed to fetch listings");
                return res.json();
            })
            .then((data) => {
                setPosts(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error(err);
                setError("Could not load your dashboard. Please verify your session.");
                setLoading(false);
            });
    }, []);

    // Safely parse JSONB fields from the backend strings
    const parseJsonB = (jsonString: string, fallbackField: string) => {
        try {
            const parsed = JSON.parse(jsonString);
            return parsed[fallbackField] || jsonString;
        } catch {
            return jsonString;
        }
    };

    if (loading) return <div className="p-8 text-center text-gray-600">Loading listings...</div>;
    if (error) return <div className="p-8 text-center text-red-500">{error}</div>;

    return (
        <main className="max-w-4xl mx-auto p-6">
            <h1 className="text-2xl font-bold mb-6 text-gray-800">Your Marketplace Dashboard</h1>

            {posts.length === 0 ? (
                <p className="text-gray-500">No listings found for this account.</p>
            ) : (
                <div className="space-y-4">
                    {posts.map((post) => {
                        const city = parseJsonB(post.location, "city");
                        const textDescription = parseJsonB(post.description, "text");

                        return (
                            <div key={post.id} className="border border-gray-200 rounded-lg p-5 flex justify-between items-center bg-white shadow-sm">
                                <div>
                                    <div className="flex items-center gap-3 mb-1">
                                        <h2 className="text-lg font-semibold text-gray-900">{post.title}</h2>
                                        <span className={`px-2 py-0.5 text-xs font-semibold rounded-full ${post.isOpen ? "bg-green-100 text-green-800" : "bg-gray-100 text-gray-800"}`}>
                                            {post.isOpen ? "Active" : "Closed"}
                                        </span>
                                    </div>
                                    <p className="text-xs text-gray-400 mb-2">Location: {city} • Created: {new Date(post.createdAt).toLocaleDateString()}</p>
                                    <p className="text-sm text-gray-600 line-clamp-2">{textDescription}</p>
                                </div>

                                <div className="ml-4 shrink-0">
                                    <PostStatusToggle
                                        postId={post.id}
                                        initialStatus={post.isOpen}
                                        onStatusChange={(newStatus) => {
                                            setPosts(prev => prev.map(p => p.id === post.id ? { ...p, isOpen: newStatus } : p));
                                        }}
                                    />
                                </div>
                            </div>
                        );
                    })}
                </div>
            )}
        </main>
    );
}