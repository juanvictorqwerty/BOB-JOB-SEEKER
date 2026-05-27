"use client";

import { createContext, useContext, useState, ReactNode, useCallback } from "react";

export type ToastType = "success" | "error" | "info" | "warning";

export interface Toast {
    id: string;
    message: string;
    type: ToastType;
    duration?: number;
}

interface ToastContextType {
    toasts: Toast[];
    addToast: (message: string, type: ToastType, duration?: number) => void;
    removeToast: (id: string) => void;
    showSuccess: (message: string, duration?: number) => void;
    showError: (message: string, duration?: number) => void;
    showInfo: (message: string, duration?: number) => void;
    showWarning: (message: string, duration?: number) => void;
}

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export function ToastProvider({ children }: { children: ReactNode }) {
    const [toasts, setToasts] = useState<Toast[]>([]);

    const removeToast = useCallback((id: string) => {
        setToasts((prev) => prev.filter((toast) => toast.id !== id));
    }, []);

    const addToast = useCallback(
        (message: string, type: ToastType, duration: number = 3500) => {
            const id = Math.random().toString(36).substr(2, 9);
            const toast: Toast = { id, message, type, duration };

            setToasts((prev) => [...prev, toast]);

            if (duration > 0) {
                setTimeout(() => removeToast(id), duration);
            }
        },
        [removeToast]
    );

    const showSuccess = useCallback(
        (message: string, duration?: number) =>
            addToast(message, "success", duration),
        [addToast]
    );

    const showError = useCallback(
        (message: string, duration?: number) =>
            addToast(message, "error", duration),
        [addToast]
    );

    const showInfo = useCallback(
        (message: string, duration?: number) =>
            addToast(message, "info", duration),
        [addToast]
    );

    const showWarning = useCallback(
        (message: string, duration?: number) =>
            addToast(message, "warning", duration),
        [addToast]
    );

    return (
        <ToastContext.Provider
            value={{ toasts, addToast, removeToast, showSuccess, showError, showInfo, showWarning }}
        >
            {children}
        </ToastContext.Provider>
    );
}

export function useToast() {
    const context = useContext(ToastContext);
    if (!context) {
        throw new Error("useToast must be used within ToastProvider");
    }
    return context;
}