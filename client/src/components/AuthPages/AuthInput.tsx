"use client";

import { useTheme } from "@/context/ThemeProvider";
import { InputHTMLAttributes } from "react";

interface AuthInputProps extends InputHTMLAttributes<HTMLInputElement> {
    label?: string;
}

export default function AuthInput({ label, className = "", ...props }: AuthInputProps) {
    const { theme } = useTheme();
    const isDark = theme === "dark";

    return (
        <div className="relative group">
            {label && (
                <label className={`block text-sm font-medium mb-2 transition-colors duration-300 ${isDark ? "text-slate-300" : "text-gray-700"}`}>
                    {label}
                </label>
            )}
            <input
                {...props}
                className={`
                    w-full px-4 py-3 rounded-lg
                    focus:outline-none focus:ring-2 transition-all duration-200
                    disabled:opacity-50
                    ${isDark
                        ? "bg-white/5 border border-white/20 text-white placeholder-slate-400 focus:ring-blue-500/50 focus:border-blue-500/50"
                        : "bg-gray-100/50 border border-gray-300/50 text-gray-900 placeholder-gray-500 focus:ring-blue-400/50 focus:border-blue-400/50"
                    }
                    ${className}
                `}
            />
            <div
                className={`
                    absolute inset-0 rounded-lg opacity-0 group-hover:opacity-100
                    transition-opacity duration-300 pointer-events-none
                    ${isDark
                        ? "bg-linear-to-r from-blue-500/20 to-purple-500/20"
                        : "bg-linear-to-r from-blue-400/10 to-purple-400/10"
                    }
                `}
            />
        </div>
    );
}