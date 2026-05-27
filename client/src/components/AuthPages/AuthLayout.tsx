"use client";

import { useTheme } from "@/context/ThemeProvider";
import { ReactNode } from "react";

interface AuthLayoutProps {
    children: ReactNode;
    title: string;
    subtitle: string;
}

export default function AuthLayout({ children, title, subtitle }: AuthLayoutProps) {
    const { theme, toggleTheme } = useTheme();
    const isDark = theme === "dark";

    return (
        <div
            className={`
                min-h-screen transition-colors duration-300
                ${isDark
                    ? "bg-linear-to-br from-slate-900 via-slate-800 to-slate-900"
                    : "bg-linear-to-br from-white via-gray-50 to-gray-100"
                }
                flex items-center justify-center p-4 relative overflow-hidden
            `}
        >
            {/* Animated background orbs */}
            <div className={`absolute -top-40 -right-40 w-80 h-80 rounded-full blur-3xl animate-pulse ${isDark ? "bg-blue-500/10" : "bg-blue-400/5"}`} />
            <div className={`absolute -bottom-40 -left-40 w-80 h-80 rounded-full blur-3xl animate-pulse delay-1000 ${isDark ? "bg-indigo-500/10" : "bg-indigo-400/5"}`} />
            <div className={`absolute top-1/2 left-1/2 w-96 h-96 rounded-full blur-3xl ${isDark ? "bg-purple-500/5" : "bg-purple-400/5"}`} />

            <div className="relative z-10 w-full max-w-md">
                <div
                    className={`
                        backdrop-blur-xl rounded-2xl shadow-2xl p-8 md:p-10
                        transition-all duration-300
                        ${isDark
                            ? "bg-white/10 border border-white/20"
                            : "bg-white/50 border border-gray-200/50"
                        }
                    `}
                >
                    {/* Header with Theme Toggle */}
                    <div className="flex items-center justify-between mb-8">
                        <div>
                            <h1 className={`text-4xl font-light tracking-tight mb-2 ${isDark ? "text-white" : "text-gray-900"}`}>
                                {title}
                            </h1>
                            <p className={`text-sm font-light ${isDark ? "text-slate-300" : "text-gray-600"}`}>
                                {subtitle}
                            </p>
                        </div>
                        <ThemeToggleButton />
                    </div>

                    {children}
                </div>

                {/* Footer */}
                <p className={`text-center text-xs mt-8 ${isDark ? "text-slate-500" : "text-gray-500"}`}>
                    By continuing, you agree to our{" "}
                    <a href="#" className={`transition-colors ${isDark ? "text-slate-300 hover:text-white" : "text-gray-700 hover:text-gray-900"}`}>
                        Terms of Service
                    </a>
                    {" "}and{" "}
                    <a href="#" className={`transition-colors ${isDark ? "text-slate-300 hover:text-white" : "text-gray-700 hover:text-gray-900"}`}>
                        Privacy Policy
                    </a>
                </p>
            </div>
        </div>
    );
}

function ThemeToggleButton() {
    const { theme, toggleTheme } = useTheme();
    const isDark = theme === "dark";

    return (
        <button
            onClick={toggleTheme}
            className={`
                p-2 rounded-lg transition-all duration-300
                ${isDark
                    ? "bg-white/10 hover:bg-white/20 text-yellow-300"
                    : "bg-gray-200/50 hover:bg-gray-300/50 text-gray-700"
                }
            `}
            aria-label="Toggle theme"
        >
            {isDark ? (
                <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z" />
                </svg>
            ) : (
                <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path fillRule="evenodd" d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.536l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.828-2.828a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414l.707.707zm4.243-5.657a1 1 0 00-1.414 1.414l.707.707a1 1 0 001.414-1.414l-.707-.707zM7.464 4.536a1 1 0 001.414-1.414L8.171 2.415a1 1 0 00-1.414 1.414l.707.707zM5.5 3a1 1 0 011 1v1a1 1 0 01-2 0V4a1 1 0 011-1zm12 12a1 1 0 01-1 1h-1a1 1 0 110-2h1a1 1 0 011 1zm-8 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zm4-17a1 1 0 01-1-1V1a1 1 0 112 0v1a1 1 0 01-1 1z" clipRule="evenodd" />
                </svg>
            )}
        </button>
    );
}