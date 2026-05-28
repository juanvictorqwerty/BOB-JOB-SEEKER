// src/app/page.tsx
"use client";

import React, { useState } from "react";
import SearchBar from "@/components/SearchBar";
import { useUnifiedSearch } from "@/hooks/infiniteSearch";
import { Briefcase, ShoppingBag, MapPin, DollarSign, RefreshCw, ImageOff } from "lucide-react";

const IMAGE_BASE_URL = "http://localhost:8080/api/images";

export default function Home() {
  const [activeSearch, setActiveSearch] = useState("");

  const {
    data,
    isLoading,
    isError,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage
  } = useUnifiedSearch(activeSearch);

  // Flatten infinite data arrays across all loaded pages
  const allJobs = data?.pages.flatMap((page) => page.jobPosts) || [];
  const allMarketplace = data?.pages.flatMap((page) => page.marketplaceListings) || [];

  // Metadata limits from the most recent page read
  const totalJobs = data?.pages[data.pages.length - 1]?.totalJobsFound || 0;
  const totalMarketplace = data?.pages[data.pages.length - 1]?.totalMarketplaceFound || 0;

  const safeParseText = (jsonString: string) => {
    try {
      const parsed = JSON.parse(jsonString);
      return parsed.text || parsed.description || "";
    } catch {
      return jsonString || "";
    }
  };

  /**
   * imageUrl is stored as a JSONB array of absolute disk paths, e.g.:
   *   ["/media/.../server/images/uuid_photo.jpg", ...]
   * We extract just the filename and point to the public serve endpoint.
   */
  const parseImageUrls = (imageUrlJson: string | null | undefined): string[] => {
    if (!imageUrlJson) return [];
    try {
      const paths: string[] = JSON.parse(imageUrlJson);
      return paths.map((p) => {
        // Extract just the filename from the absolute path
        const filename = p.split("/").pop() || p.split("\\").pop() || p;
        return `${IMAGE_BASE_URL}/${encodeURIComponent(filename)}`;
      });
    } catch {
      return [];
    }
  };

  return (
    <main className="min-h-screen bg-gray-50/50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-5xl mx-auto space-y-10">

        <div className="text-center space-y-2">
          <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight">Infinite Finder</h1>
          <p className="text-sm text-gray-500">Retrieving dual table aggregates 10 by 10 sequentially.</p>

          {/* Passing tracking function directly down into child component */}
          <SearchBar onSearchSubmit={(term) => setActiveSearch(term)} />
        </div>

        {isLoading && (
          <div className="text-center py-10 text-sm font-medium text-gray-400 animate-pulse">
            Initializing infinite stream buffers...
          </div>
        )}

        {isError && (
          <div className="text-center py-4 bg-red-50 text-red-600 rounded-xl border border-red-100 text-sm font-medium">
            Communication loss with backend cluster. Ensure Spring Boot is serving dependencies.
          </div>
        )}

        {data && (
          <div className="space-y-10">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">

              {/* JOBS SCROLL COLUMN */}
              <div className="space-y-4">
                <div className="flex items-center space-x-2 pb-2 border-b border-gray-200">
                  <Briefcase className="h-4 w-4 text-gray-700" />
                  <h2 className="font-bold text-gray-800 text-lg">Job Openings ({totalJobs})</h2>
                </div>
                {allJobs.map((job) => (
                  <div key={job.id} className="bg-white p-5 rounded-xl border border-gray-100 shadow-sm hover:shadow-md transition-all">
                    <h3 className="font-bold text-gray-900 text-base">{job.title}</h3>
                    <p className="text-xs text-gray-500 line-clamp-2 mt-1">{safeParseText(job.description)}</p>
                    <div className="flex items-center gap-4 mt-3 text-xs text-gray-400 font-medium">
                      <span className="flex items-center gap-1"><MapPin className="h-3 w-3" /> {job.city}</span>
                      {job.salary && <span className="flex items-center text-emerald-600"><DollarSign className="h-3 w-3" />{job.salary.toLocaleString()}</span>}
                    </div>
                  </div>
                ))}
                {allJobs.length === 0 && <p className="text-sm text-gray-400 py-2">No matching jobs.</p>}
              </div>

              {/* MARKETPLACE SCROLL COLUMN */}
              <div className="space-y-4">
                <div className="flex items-center space-x-2 pb-2 border-b border-gray-200">
                  <ShoppingBag className="h-4 w-4 text-gray-700" />
                  <h2 className="font-bold text-gray-800 text-lg">Marketplace ({totalMarketplace})</h2>
                </div>
                {allMarketplace.map((item) => {
                  const images = parseImageUrls(item.imageUrl);
                  return (
                    <div key={item.id} className="bg-white rounded-xl border border-gray-100 shadow-sm hover:shadow-md transition-all overflow-hidden">
                      {/* Image strip — shows up to 2 images side by side */}
                      {images.length > 0 ? (
                        <div className={`grid ${images.length > 1 ? "grid-cols-2" : "grid-cols-1"} gap-0.5 bg-gray-100`}>
                          {images.map((src, idx) => (
                            <img
                              key={idx}
                              src={src}
                              alt={`${item.title} image ${idx + 1}`}
                              className="w-full h-40 object-cover"
                              loading="lazy"
                              onError={(e) => {
                                (e.currentTarget as HTMLImageElement).style.display = "none";
                              }}
                            />
                          ))}
                        </div>
                      ) : (
                        <div className="flex items-center justify-center h-24 bg-gray-50 border-b border-gray-100">
                          <ImageOff className="h-6 w-6 text-gray-300" />
                        </div>
                      )}

                      <div className="p-5">
                        <h3 className="font-bold text-gray-900 text-base">{item.title}</h3>
                        <p className="text-xs text-gray-500 line-clamp-2 mt-1">{safeParseText(item.description)}</p>
                        <span className="inline-flex items-center gap-1 text-xs text-gray-400 font-medium mt-3">
                          <MapPin className="h-3 w-3" /> Community Core Listing
                        </span>
                      </div>
                    </div>
                  );
                })}
                {allMarketplace.length === 0 && <p className="text-sm text-gray-400 py-2">No matching items.</p>}
              </div>

            </div>

            {/* INFINITE PAGINATION TRIGGER INTERFACE */}
            {hasNextPage && (
              <div className="flex justify-center pt-4">
                <button
                  type="button"
                  onClick={() => fetchNextPage()}
                  disabled={isFetchingNextPage}
                  className="flex items-center space-x-2 bg-white hover:bg-gray-50 text-gray-700 border border-gray-200 shadow-sm px-6 py-3 rounded-xl font-semibold text-sm transition-all disabled:opacity-50"
                >
                  {isFetchingNextPage ? (
                    <>
                      <RefreshCw className="h-4 w-4 animate-spin text-gray-400" />
                      <span>Loading Next 10 Records...</span>
                    </>
                  ) : (
                    <span>Load More Results</span>
                  )}
                </button>
              </div>
            )}
          </div>
        )}

      </div>
    </main>
  );
}
