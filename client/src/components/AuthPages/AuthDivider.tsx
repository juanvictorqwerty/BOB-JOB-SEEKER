"use client";

import { useTheme } from "@/context/ThemeProvider";

export default function AuthDivider() {
    const { theme } = useTheme();
    const isDark = theme === "dark";

    return (
        <div className="relative my-6">
            <div className="absolute inset-0 flex items-center">
                <div className={`w-full border-t ${isDark ? "border-white/10" : "border-gray-300/30"}`} />
            </div>
            <div className="relative flex justify-center text-sm">
                <span className={`px-2 ${isDark ? "bg-slate-800/50 text-slate-400" : "bg-gray-50 text-gray-500"}`}>
                    or
                </span>
            </div>
        </div>
    );
}