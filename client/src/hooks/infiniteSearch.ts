// src/hooks/useUnifiedSearch.ts
import { useInfiniteQuery } from "@tanstack/react-query";
import Cookies from "js-cookie";

export interface JobPost {
    id: string;
    title: string;
    city: string;
    salary: number | null;
    description: string;
    geoLocation: string;
    createdAt: string;
    updatedAt: string | null;
    isOpened: boolean;
}

export interface MarketPlaceListing {
    id: string;
    title: string;
    location: string;
    description: string;
    imageUrl: string | null;
    createdAt: string;
    updatedAt: string | null;
    isOpen: boolean;
}

interface UnifiedSearchResponse {
    jobPosts: JobPost[];
    marketplaceListings: MarketPlaceListing[];
    totalJobsFound: number;
    totalMarketplaceFound: number;
}

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE ?? "http://localhost:8080";

async function fetchUnifiedResults(searchTerm: string, pageParam: number) {
    const params = new URLSearchParams({
        q: searchTerm,
        page: pageParam.toString(),
        size: "10", // Hardcoded 10 by 10 structure
    });

    const token = Cookies.get('token');
    const response = await fetch(`${API_BASE_URL}/search/unified?${params.toString()}`, {
        method: "GET",
        headers: token ? { Authorization: `Bearer ${token}` } : undefined,
        credentials: "include",
    });
    if (!response.ok) {
        throw new Error("Failed to load global search results");
    }
    return response.json() as Promise<UnifiedSearchResponse>;
}

export function useUnifiedSearch(searchTerm: string) {
    return useInfiniteQuery({
        queryKey: ["unifiedSearch", searchTerm],
        queryFn: ({ pageParam = 0 }) => fetchUnifiedResults(searchTerm, pageParam),
        initialPageParam: 0,
        // Calculate if more pages exist across either data domain
        getNextPageParam: (lastPage, allPages) => {
            const currentFetchedCount = allPages.length * 10;

            const hasMoreJobs = currentFetchedCount < lastPage.totalJobsFound;
            const hasMoreMarketplace = currentFetchedCount < lastPage.totalMarketplaceFound;

            // If either table has more data, return the next page index
            return (hasMoreJobs || hasMoreMarketplace) ? allPages.length : undefined;
        },
    });
}