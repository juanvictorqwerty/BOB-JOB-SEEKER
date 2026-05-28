"use client"

import Link from "next/link";
import ThemeToggle from "../ThemeToggle";

export default function NavBarClient() {
    return (
        <nav className="bg-white shadow-lg dark:bg-gray-800 transition-colors">
            <div className="container mx-auto px-4">
                <div className="flex justify-between items-center h-16">
                    <div className="text-xl font-bold text-gray-800 dark:text-white">
                        Bob Job Seeker
                    </div>
                    <div className="flex items-center space-x-6">
                        <ul className="flex space-x-6">
                            <li>
                                <Link href="/" className="text-gray-600 hover:text-gray-900 dark:text-gray-300 dark:hover:text-white transition">
                                    Home
                                </Link>
                            </li>
                            <li>
                                <Link href="/jobs" className="text-gray-600 hover:text-gray-900 dark:text-gray-300 dark:hover:text-white transition">
                                    Find Jobs
                                </Link>
                            </li>
                            <li>
                                <Link href="/companies" className="text-gray-600 hover:text-gray-900 dark:text-gray-300 dark:hover:text-white transition">
                                    Companies
                                </Link>
                            </li>
                            <li>
                                <Link href="/about" className="text-gray-600 hover:text-gray-900 dark:text-gray-300 dark:hover:text-white transition">
                                    About
                                </Link>
                            </li>
                            <li>
                                <Link href="/Account" className="text-gray-600 hover:text-gray-900 dark:text-gray-300 dark:hover:text-white transition">
                                    Account
                                </Link>
                            </li>
                        </ul>
                        <ThemeToggle />
                    </div>
                </div>
            </div>
        </nav>
    );
}