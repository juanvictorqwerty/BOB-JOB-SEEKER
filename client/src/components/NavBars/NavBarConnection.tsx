"use client";

import Link from "next/link";
import ThemeToggle from "../ThemeToggle";

export default function NavBarConnection() {
    return (
        <nav className="bg-linear-to-r from-blue-600 to-indigo-600 text-white shadow-lg">
            <div className="container mx-auto px-4">
                <div className="flex justify-between items-center h-16">
                    <Link href="/" className="text-xl font-bold hover:text-blue-100 transition">
                        Bob Job Seeker
                    </Link>
                    <div className="flex items-center space-x-4">
                        <ThemeToggle />
                        <Link
                            href="/auth/login"
                            className="px-4 py-2 rounded-lg bg-white/10 hover:bg-white/20 transition"
                        >
                            Login
                        </Link>
                        <Link
                            href="/auth/register"
                            className="px-4 py-2 rounded-lg bg-white text-blue-600 hover:bg-gray-100 transition"
                        >
                            Sign Up
                        </Link>
                    </div>
                </div>
            </div>
        </nav>
    );
}