// src/components/SearchBar.tsx
"use client";

import React, { useState } from "react";

interface SearchBarProps {
    onSearchSubmit: (term: string) => void;
}

export default function SearchBar({ onSearchSubmit }: SearchBarProps) {
    const [inputValue, setInputValue] = useState("");

    const handleSubmit = () => {
        onSearchSubmit(inputValue.trim());
    };

    return (
        <div className="flex items-center justify-center p-5">
            <div className="rounded-xl bg-gray-100 p-4 border border-gray-200/60 shadow-sm w-full max-w-lg">
                <div className="flex items-center bg-white rounded-lg border border-gray-200 overflow-hidden focus-within:ring-2 focus-within:ring-blue-500/20 transition-all">

                    <div className="flex w-10 h-10 items-center justify-center bg-gray-50/50 border-r border-gray-100">
                        <svg viewBox="0 0 20 20" aria-hidden="true" className="w-4 h-4 fill-gray-400">
                            <path d="M16.72 17.78a.75.75 0 1 0 1.06-1.06l-1.06 1.06ZM9 14.5A5.5 5.5 0 0 1 3.5 9H2a7 7 0 0 0 7 7v-1.5ZM3.5 9A5.5 5.5 0 0 1 9 3.5V2a7 7 0 0 0-7 7h1.5ZM9 3.5A5.5 5.5 0 0 1 14.5 9H16a7 7 0 0 0-7-7v1.5Zm3.89 10.45 3.83 3.83 1.06-1.06-3.83-3.83-1.06 1.06ZM14.5 9a5.48 5.48 0 0 1-1.61 3.89l1.06 1.06A6.98 6.98 0 0 0 16 9h-1.5Zm-1.61 3.89A5.48 5.48 0 0 1 9 14.5V16a6.98 6.98 0 0 0 4.95-2.05l-1.06-1.06Z"></path>
                        </svg>
                    </div>

                    <input
                        type="text"
                        value={inputValue}
                        onChange={(e) => setInputValue(e.target.value)}
                        onKeyDown={(e) => e.key === "Enter" && handleSubmit()}
                        className="w-full px-3 py-2 text-sm font-medium text-gray-700 outline-none placeholder-gray-400 bg-transparent"
                        placeholder="Search all records..."
                    />

                    <button
                        type="button"
                        onClick={handleSubmit}
                        className="bg-blue-600 px-5 py-2.5 text-sm font-semibold text-white hover:bg-blue-700 transition-colors shrink-0"
                    >
                        Search
                    </button>
                </div>
            </div>
        </div>
    );
}