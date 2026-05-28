"use client";

import { usePathname } from "next/navigation";
import NavBarConnection from "./NavBarConnection";
import NavBarAdmin from "./NavBarAdmin";
import NavBarCompany from "./NavBarCompany";
import NavBarClient from "./NavBarClient";

export default function NavBar() {
    const pathname = usePathname();

    // Auth pages (login, register, etc.)
    if (pathname?.startsWith("/auth/")) {
        return <NavBarConnection />;
    }

    // Admin pages
    if (pathname?.startsWith("/admin/")) {
        return <NavBarAdmin />;
    }

    // Company pages
    if (pathname?.startsWith("/company/")) {
        return <NavBarCompany />;
    }

    // Default for all other pages (client/home pages)
    return <NavBarClient />;
}