"use client";

import { useTheme } from "@/context/ThemeProvider";

export default function ThemeToggle() {
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