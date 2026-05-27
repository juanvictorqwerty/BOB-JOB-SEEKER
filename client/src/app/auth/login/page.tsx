"use client";

import Link from "next/link";
import { useState } from "react";
import { useRouter } from "next/navigation"; // Imported for navigation

export default function Login() { // Removed 'async'
    const router = useRouter();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e: React.SubmitEvent) => {
        e.preventDefault();
        setIsLoading(true);

        try {
            const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE}/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email,
                    password,
                }),
            });

            if (response.ok) {
                const data = await response.json();
                alert("Login successful!");
                // Redirect based on user rank returned from your action
                if (data.rank === 0) {
                    router.push("/admin");
                } else {
                    router.push("/");
                }
            } else {
                const data = await response.json();
                alert(data.message || "Invalid credentials");
            }
        } catch (error) {
            console.error(error);
            alert("An unexpected error occurred.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div>
            <div className="h-96 flex items-center justify-center bg-gradient-to-r from-purple-400 via-pink-500 to-red-500">
                <div className="relative">
                    <div className="absolute -top-2 -left-2 -right-2 -bottom-2 rounded-lg bg-gradient-to-r from-purple-400 via-pink-500 to-red-500 shadow-lg animate-pulse"></div>
                    <div id="form-container" className="bg-white p-16 rounded-lg shadow-2xl w-80 relative z-10 transform transition duration-500 ease-in-out">
                        <h2 id="form-title" className="text-center text-3xl font-bold mb-10 text-gray-800">Login</h2>

                        <form className="space-y-5" onSubmit={handleSubmit}>
                            <input
                                className="w-full h-12 border border-gray-800 px-3 rounded-lg"
                                placeholder="Email"
                                id="email"
                                name="email"
                                type="email" // Changed from text to email for basic validation
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                            <input
                                className="w-full h-12 border border-gray-800 px-3 rounded-lg"
                                placeholder="Password"
                                id="password"
                                name="password"
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                            <button
                                className="w-full h-12 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline disabled:opacity-50"
                                type="submit"
                                disabled={isLoading}
                            >
                                {isLoading ? "Logging in..." : "Sign in"}
                            </button>
                            <a className="text-blue-500 hover:text-blue-800 text-sm block" href="#">Forgot Password?</a>
                        </form>

                        <div className="mt-4"> {/* Added minor margin wrapping for spacing */}
                            <Link href="/auth/signup">
                                <button className="w-full h-12 bg-gray-200 hover:bg-gray-300 text-gray-800 font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                                    Create Account
                                </button>
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}