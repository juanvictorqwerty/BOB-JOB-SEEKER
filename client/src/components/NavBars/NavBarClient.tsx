"use client"

import Link from "next/link";


export default function NavBarClient() {
    return (
        <nav className={`transition-colors bg-white`}>
            <div className="container mx-auto px-4">
                <div className="flex justify-between items-center h-16">
                    <div className="text-xl font-bold text-gray-800">
                        Bob Job Seeker
                    </div>
                    <div className="flex items-center space-x-6">
                        <ul className="flex space-x-6">
                            <li>
                                <Link href="/" className="text-gray-600 hover:text-gray-900 transition">
                                    Home
                                </Link>
                            </li>

                            <li>
                                <Link href="/userPost" className="text-gray-600 hover:text-gray-900 transition">
                                    My Posts
                                </Link>
                            </li>
                            <li>
                                <Link href="/Account" className="text-gray-600 hover:text-gray-900 transition">
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