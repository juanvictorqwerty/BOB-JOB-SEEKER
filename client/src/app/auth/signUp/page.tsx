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

export default function SignUp() {
    const router = useRouter();
    const { showSuccess, showError } = useToast();
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e: React.SubmitEvent) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            showError("Passwords do not match");
            return;
        }

        setIsLoading(true);

        try {
            const response = await fetch(
                `${process.env.NEXT_PUBLIC_API_BASE}/auth/register/regular`,
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email, username, password }),
                }
            );

            if (response.ok) {
                const data = await response.json();
                showSuccess(data.message || "Signup successful!");
                Cookies.set("token", data.token, { expires: 30, sameSite: "strict" });
                Cookies.set("rank", data.rank.toString(), { expires: 30, sameSite: "strict" });

                setTimeout(() => {
                    router.push(data.rank === 0 ? "/admin" : "/");
                }, 1500);
            } else {
                const data = await response.json();
                showError(data.message || "Failed to signup");
            }
        } catch (error) {
            console.error(error);
            showError("Connection error. Please try again.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <AuthLayout title="Welcome" subtitle="Create your account">
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
                    type="text"
                    placeholder="your username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
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
                <AuthInput
                    type="password"
                    placeholder="Re-type password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                    disabled={isLoading}
                />

                <AuthButton type="submit" isLoading={isLoading} className="mt-6">
                    Sign Up
                </AuthButton>
            </form>

            <AuthDivider />

            <Link href="/auth/login" className="block">
                <AuthButton variant="secondary">Login</AuthButton>
            </Link>
        </AuthLayout>
    );
}