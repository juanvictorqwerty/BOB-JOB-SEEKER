"use client";

import { useState } from "react";
import Cookies from "js-cookie";
import { createPortal } from "react-dom";
import { useToast } from "@/context/ToastContext";
import { ShieldCheck, X, Loader2, Monitor, Smartphone, Globe } from "lucide-react";

interface KeepOneModalProps {
    isOpen: boolean;
    onClose: () => void;
}

export default function KeepOneModal({ isOpen, onClose }: KeepOneModalProps) {
    const { showSuccess, showError } = useToast();
    const [isLoading, setIsLoading] = useState(false);

    const handleKeepOne = async () => {
        const token = Cookies.get("token");
        if (!token) {
            showError("Session expired. Please log in again.");
            return;
        }

        setIsLoading(true);
        try {
            const response = await fetch(
                `${process.env.NEXT_PUBLIC_API_BASE}/auth/keep-one`,
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ token }),
                }
            );

            if (response.ok) {
                showSuccess("All other sessions have been revoked");
                onClose();
            } else {
                const data = await response.json();
                showError(data.message || "Failed to revoke other sessions");
            }
        } catch (error) {
            showError("Network error. Please try again.");
        } finally {
            setIsLoading(false);
        }
    };

    if (!isOpen) return null;

    return createPortal(
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 animate-in fade-in duration-200">
            {/* Backdrop with blur */}
            <div
                className="absolute inset-0 bg-black/40 backdrop-blur-sm"
                onClick={onClose}
            />

            {/* Modal Card */}
            <div className="relative w-full max-w-md bg-white rounded-2xl shadow-2xl border border-gray-100 overflow-hidden animate-in zoom-in-95 duration-200">

                {/* Header with icon */}
                <div className="bg-gradient-to-br from-emerald-50 to-teal-50 p-6 text-center border-b border-gray-100">
                    <div className="mx-auto w-14 h-14 bg-white rounded-full flex items-center justify-center shadow-sm mb-3">
                        <ShieldCheck className="w-7 h-7 text-emerald-600" />
                    </div>
                    <h2 className="text-xl font-bold text-gray-900">Keep This Session</h2>
                    <p className="text-sm text-gray-500 mt-1">Security action required</p>
                </div>

                {/* Content */}
                <div className="p-6 space-y-4">
                    <p className="text-gray-600 text-center leading-relaxed">
                        This will <span className="font-semibold text-gray-900">sign out all other devices</span> and keep only your current session active.
                    </p>

                    {/* Device Preview */}
                    <div className="bg-gray-50 rounded-xl p-4 flex items-center gap-3 border border-gray-100">
                        <div className="w-10 h-10 bg-emerald-100 rounded-lg flex items-center justify-center">
                            <Monitor className="w-5 h-5 text-emerald-600" />
                        </div>
                        <div className="flex-1">
                            <p className="text-sm font-medium text-gray-900">Current Device</p>
                            <p className="text-xs text-gray-500">This browser session will remain active</p>
                        </div>
                        <div className="w-2 h-2 bg-emerald-500 rounded-full animate-pulse" />
                    </div>

                    <div className="bg-amber-50 border border-amber-100 rounded-lg p-3 flex gap-2">
                        <div className="text-amber-600 mt-0.5">
                            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                            </svg>
                        </div>
                        <p className="text-xs text-amber-800 leading-relaxed">
                            Other active sessions on mobile apps or browsers will be immediately terminated.
                        </p>
                    </div>
                </div>

                {/* Actions */}
                <div className="p-6 pt-2 flex gap-3">
                    <button
                        onClick={onClose}
                        className="flex-1 px-4 py-2.5 text-sm font-medium text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-xl transition-colors"
                        disabled={isLoading}
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleKeepOne}
                        disabled={isLoading}
                        className="flex-1 px-4 py-2.5 text-sm font-medium text-white bg-emerald-600 hover:bg-emerald-700 disabled:opacity-50 disabled:cursor-not-allowed rounded-xl transition-all flex items-center justify-center gap-2 shadow-lg shadow-emerald-600/20"
                    >
                        {isLoading ? (
                            <>
                                <Loader2 className="w-4 h-4 animate-spin" />
                                Processing...
                            </>
                        ) : (
                            "Keep This Session"
                        )}
                    </button>
                </div>

                {/* Close X */}
                <button
                    onClick={onClose}
                    className="absolute top-4 right-4 p-1.5 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-lg transition-colors"
                >
                    <X className="w-5 h-5" />
                </button>
            </div>
        </div>,
        document.body
    );
}