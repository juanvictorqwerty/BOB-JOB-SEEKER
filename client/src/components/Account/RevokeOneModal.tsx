"use client";

import { useState } from "react";
import Cookies from "js-cookie";
import { createPortal } from "react-dom";
import { useToast } from "@/context/ToastContext";
import { LogOut, X, Loader2, DoorOpen } from "lucide-react";

interface RevokeOneModalProps {
    isOpen: boolean;
    onClose: () => void;
}

export default function RevokeOneModal({ isOpen, onClose }: RevokeOneModalProps) {
    const { showSuccess, showError } = useToast();
    const [isLoading, setIsLoading] = useState(false);

    const handleRevokeOne = async () => {
        const token = Cookies.get("token");
        if (!token) {
            showError("No active session found");
            return;
        }

        setIsLoading(true);
        try {
            const response = await fetch(
                `${process.env.NEXT_PUBLIC_API_BASE}/auth/revoke-one`,
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ token }),
                }
            );

            if (response.ok) {
                showSuccess("Signed out successfully");
                setTimeout(() => {
                    Cookies.remove("token");
                    Cookies.remove("rank");
                    window.location.href = "/auth/login";
                }, 800);
            } else {
                const data = await response.json();
                showError(data.message || "Failed to sign out");
                setIsLoading(false);
            }
        } catch (error) {
            showError("Connection error. Please try again.");
            setIsLoading(false);
        }
    };

    if (!isOpen) return null;

    return createPortal(
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 animate-in fade-in duration-200">
            <div
                className="absolute inset-0 bg-black/40 backdrop-blur-sm"
                onClick={!isLoading ? onClose : undefined}
            />

            <div className="relative w-full max-w-sm bg-white rounded-2xl shadow-2xl border border-gray-100 overflow-hidden animate-in zoom-in-95 duration-200">

                {/* Minimal Header */}
                <div className="p-6 pb-0">
                    <div className="w-12 h-12 bg-orange-100 rounded-xl flex items-center justify-center mb-4">
                        <DoorOpen className="w-6 h-6 text-orange-600" />
                    </div>
                    <h2 className="text-lg font-bold text-gray-900">Sign Out</h2>
                </div>

                <div className="p-6 pt-3">
                    <p className="text-gray-600 text-sm leading-relaxed">
                        Are you sure you want to sign out from this device? You will need to log in again to access your account.
                    </p>
                </div>

                {/* Actions */}
                <div className="p-6 pt-0 flex gap-3">
                    <button
                        onClick={onClose}
                        disabled={isLoading}
                        className="flex-1 px-4 py-2.5 text-sm font-medium text-gray-700 bg-gray-100 hover:bg-gray-200 disabled:opacity-50 rounded-xl transition-colors"
                    >
                        Stay Signed In
                    </button>
                    <button
                        onClick={handleRevokeOne}
                        disabled={isLoading}
                        className="flex-1 px-4 py-2.5 text-sm font-medium text-white bg-gray-900 hover:bg-gray-800 disabled:opacity-50 disabled:cursor-not-allowed rounded-xl transition-all flex items-center justify-center gap-2"
                    >
                        {isLoading ? (
                            <Loader2 className="w-4 h-4 animate-spin" />
                        ) : (
                            <>
                                <LogOut className="w-4 h-4" />
                                Sign Out
                            </>
                        )}
                    </button>
                </div>

                <button
                    onClick={onClose}
                    disabled={isLoading}
                    className="absolute top-4 right-4 p-1.5 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-lg transition-colors disabled:opacity-50"
                >
                    <X className="w-4 h-4" />
                </button>
            </div>
        </div>,
        document.body
    );
}