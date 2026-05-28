"use client";

import { useState } from "react";
import Cookies from "js-cookie";
import { useToast } from "@/context/ToastContext";
import { createPortal } from "react-dom";
import { LogOut, X, Loader2, AlertTriangle, Monitor, Smartphone, Tablet } from "lucide-react";

interface RevokeAllModalProps {
    isOpen: boolean;
    onClose: () => void;
}

export default function RevokeAllModal({ isOpen, onClose }: RevokeAllModalProps) {
    const { showSuccess, showError } = useToast();
    const [isLoading, setIsLoading] = useState(false);

    const handleRevokeAll = async () => {
        const token = Cookies.get("token");
        if (!token) {
            showError("No active session found");
            return;
        }

        setIsLoading(true);
        try {
            const response = await fetch(
                `${process.env.NEXT_PUBLIC_API_BASE}/auth/revoke-all`,
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ token }),
                }
            );

            if (response.ok) {
                const data = await response.json();
                showSuccess(data.message || "Signed out from all devices");

                // Small delay for toast visibility before redirect
                setTimeout(() => {
                    Cookies.remove("token");
                    Cookies.remove("rank");
                    window.location.href = "/auth/login";
                }, 1500);
            } else {
                const data = await response.json();
                showError(data.message || "Failed to revoke sessions");
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
                className="absolute inset-0 bg-black/50 backdrop-blur-sm"
                onClick={!isLoading ? onClose : undefined}
            />

            <div className="relative w-full max-w-md bg-white rounded-2xl shadow-2xl border border-red-100 overflow-hidden animate-in zoom-in-95 duration-200">

                {/* Alert Header */}
                <div className="bg-gradient-to-br from-red-50 to-rose-50 p-6 text-center border-b border-red-100">
                    <div className="mx-auto w-14 h-14 bg-white rounded-full flex items-center justify-center shadow-sm mb-3">
                        <AlertTriangle className="w-7 h-7 text-red-600" />
                    </div>
                    <h2 className="text-xl font-bold text-gray-900">Sign Out Everywhere</h2>
                    <p className="text-sm text-red-600 font-medium mt-1">Destructive action</p>
                </div>

                <div className="p-6 space-y-5">
                    <p className="text-gray-600 text-center leading-relaxed">
                        This will immediately <span className="font-semibold text-red-700">terminate all sessions</span> including this one. You will need to log in again.
                    </p>

                    {/* Affected Devices Visualization */}
                    <div className="flex justify-center gap-4 py-2">
                        <div className="flex flex-col items-center gap-2 opacity-50">
                            <div className="w-10 h-10 bg-gray-100 rounded-lg flex items-center justify-center">
                                <Monitor className="w-5 h-5 text-gray-500" />
                            </div>
                            <span className="text-[10px] text-gray-400">Web</span>
                        </div>
                        <div className="flex flex-col items-center gap-2 opacity-50">
                            <div className="w-10 h-10 bg-gray-100 rounded-lg flex items-center justify-center">
                                <Smartphone className="w-5 h-5 text-gray-500" />
                            </div>
                            <span className="text-[10px] text-gray-400">Mobile</span>
                        </div>
                        <div className="flex flex-col items-center gap-2 opacity-50">
                            <div className="w-10 h-10 bg-gray-100 rounded-lg flex items-center justify-center">
                                <Tablet className="w-5 h-5 text-gray-500" />
                            </div>
                            <span className="text-[10px] text-gray-400">Tablet</span>
                        </div>
                    </div>

                    <div className="bg-red-50 border border-red-100 rounded-xl p-4">
                        <div className="flex items-start gap-3">
                            <LogOut className="w-5 h-5 text-red-600 mt-0.5 shrink-0" />
                            <div>
                                <p className="text-sm font-semibold text-red-900">You will be signed out</p>
                                <p className="text-xs text-red-700 mt-1 leading-relaxed">
                                    This includes your current session. Make sure you remember your password before proceeding.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Actions */}
                <div className="p-6 pt-2 flex gap-3">
                    <button
                        onClick={onClose}
                        disabled={isLoading}
                        className="flex-1 px-4 py-2.5 text-sm font-medium text-gray-700 bg-gray-100 hover:bg-gray-200 disabled:opacity-50 rounded-xl transition-colors"
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleRevokeAll}
                        disabled={isLoading}
                        className="flex-1 px-4 py-2.5 text-sm font-medium text-white bg-red-600 hover:bg-red-700 disabled:opacity-50 disabled:cursor-not-allowed rounded-xl transition-all flex items-center justify-center gap-2 shadow-lg shadow-red-600/20"
                    >
                        {isLoading ? (
                            <>
                                <Loader2 className="w-4 h-4 animate-spin" />
                                Signing out...
                            </>
                        ) : (
                            <>
                                <LogOut className="w-4 h-4" />
                                Revoke All
                            </>
                        )}
                    </button>
                </div>

                <button
                    onClick={onClose}
                    disabled={isLoading}
                    className="absolute top-4 right-4 p-1.5 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-lg transition-colors disabled:opacity-50"
                >
                    <X className="w-5 h-5" />
                </button>
            </div>
        </div>,
        document.body
    );
}