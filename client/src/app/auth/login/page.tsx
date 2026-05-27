"use client";

import Link from "next/link";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { useTheme } from "@/context/ThemeProvider";
import { useToast } from "@/context/ToastContext";

export default function Login() {
    const router = useRouter();
    const { theme, toggleTheme } = useTheme();
    const { showSuccess, showError } = useToast();
    const isDark = theme === "dark";

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsLoading(true);

        try {
            const response = await fetch(
                `${process.env.NEXT_PUBLIC_API_BASE}/auth/login`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ email, password }),
                }
            );

            if (response.ok) {
                const data = await response.json();
                showSuccess(data.message || "Login successful!");

                setTimeout(() => {
                    if (data.rank === 0) {
                        router.push("/admin");
                    } else {
                        router.push("/");
                    }
                }, 1500);
            } else {
                const data = await response.json();
                showError(data.message || "Invalid credentials");
            }
        } catch (error) {
            console.error(error);
            showError("Connection error. Please try again.");
        } finally {
            setIsLoading(false);
        }
    };

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
            <div
                className={`
                    absolute -top-40 -right-40 w-80 h-80 rounded-full blur-3xl animate-pulse
                    ${isDark ? "bg-blue-500/10" : "bg-blue-400/5"}
                `}
            ></div>
            <div
                className={`
                    absolute -bottom-40 -left-40 w-80 h-80 rounded-full blur-3xl animate-pulse delay-1000
                    ${isDark ? "bg-indigo-500/10" : "bg-indigo-400/5"}
                `}
            ></div>
            <div
                className={`
                    absolute top-1/2 left-1/2 w-96 h-96 rounded-full blur-3xl
                    ${isDark ? "bg-purple-500/5" : "bg-purple-400/5"}
                `}
            ></div>

            <div className="relative z-10 w-full max-w-md">
                {/* Card Container */}
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
                            <h1
                                className={`
                                    text-4xl font-light tracking-tight mb-2
                                    ${isDark ? "text-white" : "text-gray-900"}
                                `}
                            >
                                Welcome
                            </h1>
                            <p
                                className={`
                                    text-sm font-light
                                    ${isDark ? "text-slate-300" : "text-gray-600"}
                                `}
                            >
                                Sign in to your account
                            </p>
                        </div>

                        {/* Theme Toggle Button */}
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
                                <svg
                                    className="w-5 h-5"
                                    fill="currentColor"
                                    viewBox="0 0 20 20"
                                >
                                    <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z" />
                                </svg>
                            ) : (
                                <svg
                                    className="w-5 h-5"
                                    fill="currentColor"
                                    viewBox="0 0 20 20"
                                >
                                    <path
                                        fillRule="evenodd"
                                        d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.536l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.828-2.828a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414l.707.707zm4.243-5.657a1 1 0 00-1.414 1.414l.707.707a1 1 0 001.414-1.414l-.707-.707zM7.464 4.536a1 1 0 001.414-1.414L8.171 2.415a1 1 0 00-1.414 1.414l.707.707zM5.5 3a1 1 0 011 1v1a1 1 0 01-2 0V4a1 1 0 011-1zm12 12a1 1 0 01-1 1h-1a1 1 0 110-2h1a1 1 0 011 1zm-8 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zm4-17a1 1 0 01-1-1V1a1 1 0 112 0v1a1 1 0 01-1 1z"
                                        clipRule="evenodd"
                                    />
                                </svg>
                            )}
                        </button>
                    </div>

                    {/* Form */}
                    <form onSubmit={handleSubmit} className="space-y-4">
                        {/* Email Input */}
                        <div className="relative group">
                            <input
                                type="email"
                                id="email"
                                name="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="your@email.com"
                                required
                                disabled={isLoading}
                                className={`
                                    w-full px-4 py-3 rounded-lg
                                    focus:outline-none focus:ring-2 transition-all duration-200
                                    disabled:opacity-50
                                    ${isDark
                                        ? "bg-white/5 border border-white/20 text-white placeholder-slate-400 focus:ring-blue-500/50 focus:border-blue-500/50"
                                        : "bg-gray-100/50 border border-gray-300/50 text-gray-900 placeholder-gray-500 focus:ring-blue-400/50 focus:border-blue-400/50"
                                    }
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
                            ></div>
                        </div>

                        {/* Password Input */}
                        <div className="relative group">
                            <input
                                type="password"
                                id="password"
                                name="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="••••••••"
                                required
                                disabled={isLoading}
                                className={`
                                    w-full px-4 py-3 rounded-lg
                                    focus:outline-none focus:ring-2 transition-all duration-200
                                    disabled:opacity-50
                                    ${isDark
                                        ? "bg-white/5 border border-white/20 text-white placeholder-slate-400 focus:ring-blue-500/50 focus:border-blue-500/50"
                                        : "bg-gray-100/50 border border-gray-300/50 text-gray-900 placeholder-gray-500 focus:ring-blue-400/50 focus:border-blue-400/50"
                                    }
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
                            ></div>
                        </div>

                        {/* Submit Button */}
                        <button
                            type="submit"
                            disabled={isLoading}
                            className={`
                                w-full py-3 px-4 rounded-lg font-medium
                                transition-all duration-200 mt-6
                                disabled:opacity-50 disabled:cursor-not-allowed
                                flex items-center justify-center gap-2
                                ${isDark
                                    ? "bg-linear-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 text-white hover:shadow-lg hover:shadow-blue-500/25"
                                    : "bg-linear-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 text-white hover:shadow-lg hover:shadow-blue-500/20"
                                }
                            `}
                        >
                            {isLoading ? (
                                <>
                                    <svg
                                        className="animate-spin h-5 w-5"
                                        xmlns="http://www.w3.org/2000/svg"
                                        fill="none"
                                        viewBox="0 0 24 24"
                                    >
                                        <circle
                                            className="opacity-25"
                                            cx="12"
                                            cy="12"
                                            r="10"
                                            stroke="currentColor"
                                            strokeWidth="4"
                                        ></circle>
                                        <path
                                            className="opacity-75"
                                            fill="currentColor"
                                            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                                        ></path>
                                    </svg>
                                    Signing in...
                                </>
                            ) : (
                                "Sign in"
                            )}
                        </button>
                    </form>

                    {/* Divider */}
                    <div className="relative my-6">
                        <div className="absolute inset-0 flex items-center">
                            <div
                                className={`
                                    w-full border-t
                                    ${isDark ? "border-white/10" : "border-gray-300/30"}
                                `}
                            ></div>
                        </div>
                        <div className="relative flex justify-center text-sm">
                            <span
                                className={`
                                    px-2
                                    ${isDark
                                        ? "bg-slate-800/50 text-slate-400"
                                        : "bg-gray-50 text-gray-500"
                                    }
                                `}
                            >
                                or
                            </span>
                        </div>
                    </div>

                    {/* Create Account Button */}
                    <Link href="/auth/signup" className="block">
                        <button
                            type="button"
                            className={`
                                w-full py-3 px-4 rounded-lg font-medium
                                transition-all duration-200
                                ${isDark
                                    ? "border border-white/20 text-white hover:border-white/40 hover:bg-white/5"
                                    : "border border-gray-300/50 text-gray-900 hover:border-gray-400/50 hover:bg-gray-100/30"
                                }
                            `}
                        >
                            Create Account
                        </button>
                    </Link>

                    {/* Forgot Password Link */}
                    <div className="text-center mt-4">
                        <a
                            href="#"
                            className={`
                                text-sm transition-colors duration-200
                                ${isDark
                                    ? "text-slate-400 hover:text-blue-400"
                                    : "text-gray-600 hover:text-blue-600"
                                }
                            `}
                        >
                            Forgot password?
                        </a>
                    </div>
                </div>

                {/* Footer Text */}
                <p
                    className={`
                        text-center text-xs mt-8
                        ${isDark ? "text-slate-500" : "text-gray-500"}
                    `}
                >
                    By signing in, you agree to our{" "}
                    <a
                        href="#"
                        className={`
                            transition-colors
                            ${isDark
                                ? "text-slate-300 hover:text-white"
                                : "text-gray-700 hover:text-gray-900"
                            }
                        `}
                    >
                        Terms of Service
                    </a>
                    {" "}and{" "}
                    <a
                        href="#"
                        className={`
                            transition-colors
                            ${isDark
                                ? "text-slate-300 hover:text-white"
                                : "text-gray-700 hover:text-gray-900"
                            }
                        `}
                    >
                        Privacy Policy
                    </a>
                </p>
            </div>
        </div>
    );
}