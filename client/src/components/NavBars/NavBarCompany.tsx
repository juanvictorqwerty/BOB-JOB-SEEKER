"use client";

import Link from "next/link";


export default function NavBarCompany() {
    return (
        <nav className="bg-blue-800 text-white shadow-lg">
            <div className="container mx-auto px-4">
                <div className="flex justify-between items-center h-16">
                    <div className="text-xl font-bold">Bob Job Seeker - Company Portal</div>
                    <div className="flex items-center space-x-6">
                        <ul className="flex space-x-6">
                            <li>
                                <Link href="/company/dashboard" className="hover:text-blue-200 transition">
                                    Dashboard
                                </Link>
                            </li>
                            <li>
                                <Link href="/company/jobs" className="hover:text-blue-200 transition">
                                    My Jobs
                                </Link>
                            </li>
                            <li>
                                <Link href="/company/applications" className="hover:text-blue-200 transition">
                                    Applications
                                </Link>
                            </li>
                            <li>
                                <Link href="/company/post-job" className="hover:text-blue-200 transition">
                                    Post Job
                                </Link>
                            </li>
                            <li>
                                <Link href="/Account" className="hover:text-blue-200 transition">
                                    Account
                                </Link>
                            </li>
                        </ul>

                    </div>
                </div>
            </div>
        </nav>
    );
}