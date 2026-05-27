"use client";

import Link from "next/link";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { useToast } from "@/context/ToastContext";
import Cookies from "js-cookie";
import AuthLayout from "@/components/AuthPages/AuthLayout";
import AuthInput from "@/components/AuthPages/AuthInput";
import AuthButton from "@/components/AuthPages/AuthButton";
import AuthDivider from "@/components/AuthPages/AuthDivider";

export default function Login() {
    const router = useRouter();
    const { showSuccess, showError } = useToast();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);

        try {
            const response = await fetch(
                `${process.env.NEXT_PUBLIC_API_BASE}/auth/login`,
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email, password }),
                }
            );

            if (response.ok) {
                const data = await response.json();
                showSuccess(data.message || "Login successful!");
                Cookies.set("token", data.token, { expires: 30, sameSite: "strict" });
                Cookies.set("rank", data.rank.toString(), { expires: 30, sameSite: "strict" });

                setTimeout(() => {
                    router.push(data.rank === 0 ? "/admin" : "/");
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
        <AuthLayout title="Welcome" subtitle="Sign in to your account">
            <form onSubmit={handleSubmit} className="space-y-4">
                <AuthInput
                    type="email"
                    placeholder="your@email.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    disabled={isLoading}
                />
                <AuthInput
                    type="password"
                    placeholder="••••••••"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    disabled={isLoading}
                />

                <div className="text-right">
                    <a href="#" className="text-sm text-blue-500 hover:text-blue-600 transition-colors">
                        Forgot password?
                    </a>
                </div>

                <AuthButton type="submit" isLoading={isLoading}>
                    Sign in
                </AuthButton>
            </form>

            <AuthDivider />

            <Link href="/auth/signup" className="block">
                <AuthButton variant="secondary">Create Account</AuthButton>
            </Link>
        </AuthLayout>
    );
}