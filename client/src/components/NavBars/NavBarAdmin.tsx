"use client";

import Link from "next/link";
import ThemeToggle from "../ThemeToggle";

export default function NavBarAdmin() {
    return (
        <nav className="bg-gray-900 text-white shadow-lg">
            <div className="container mx-auto px-4">
                <div className="flex justify-between items-center h-16">
                    <div className="text-xl font-bold">Bob Job Seeker - Admin</div>
                    <div className="flex items-center space-x-6">
                        <ul className="flex space-x-6">
                            <li>
                                <Link href="/admin/dashboard" className="hover:text-gray-300 transition">
                                    Dashboard
                                </Link>
                            </li>
                            <li>
                                <Link href="/admin/users" className="hover:text-gray-300 transition">
                                    Users
                                </Link>
                            </li>
                            <li>
                                <Link href="/admin/jobs" className="hover:text-gray-300 transition">
                                    Jobs
                                </Link>
                            </li>
                            <li>
                                <Link href="/admin/reports" className="hover:text-gray-300 transition">
                                    Reports
                                </Link>
                            </li>
                            <li>
                                <Link href="/Account" className="hover:text-gray-300 transition">
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