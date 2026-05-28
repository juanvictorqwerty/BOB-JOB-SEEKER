"use client";

import { useState } from "react";

interface PostStatusToggleProps {
    postId: string;
    initialStatus: boolean;
    onStatusChange?: (newStatus: boolean) => void;
}

export function PostStatusToggle({ postId, initialStatus, onStatusChange }: PostStatusToggleProps) {
    const [isOpen, setIsOpen] = useState<boolean>(initialStatus);
    const [isLoading, setIsLoading] = useState<boolean>(false);

    const handleToggle = async () => {
        setIsLoading(true);
        try {
            // Direct update via the PATCH endpoint
            const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE}/marketplace/${postId}/status?isOpen=${!isOpen}`, {
                method: "PATCH",
                credentials: "include"
            });

            if (!response.ok) throw new Error("Failed to update status");

            setIsOpen(!isOpen);
            if (onStatusChange) onStatusChange(!isOpen);
        } catch (error) {
            console.error("Error updating post status:", error);
            alert("Failed to update post status. Please try again.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <button
            onClick={handleToggle}
            disabled={isLoading}
            className={`px-4 py-2 rounded-md font-medium text-sm transition-all duration-200 ${isLoading ? "bg-gray-300 text-gray-500 cursor-not-allowed" :
                isOpen
                    ? "bg-red-100 hover:bg-red-200 text-red-700"
                    : "bg-green-100 hover:bg-green-200 text-green-700"
                }`}
        >
            {isLoading ? "Updating..." : isOpen ? "Close Post" : "Reopen Post"}
        </button>
    );
}