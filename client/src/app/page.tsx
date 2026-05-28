// src/app/page.tsx
"use client";

import React, { useState } from "react";
import SearchBar from "@/components/SearchBar";
import { useUnifiedSearch } from "@/hooks/infiniteSearch";
import type { JobPost, MarketPlaceListing } from "@/hooks/infiniteSearch";
import {
  Briefcase, ShoppingBag, MapPin, DollarSign, RefreshCw,
  ImageOff, ChevronDown, ChevronUp, Calendar, Clock,
  CheckCircle, XCircle, Globe,
} from "lucide-react";

const IMAGE_BASE_URL = process.env.NEXT_PUBLIC_API_BASE
  ? `${process.env.NEXT_PUBLIC_API_BASE}/images`
  : "http://localhost:8080/api/images";

// ─── helpers ────────────────────────────────────────────────────────────────

function safeParseText(jsonString: string): string {
  try {
    const parsed = JSON.parse(jsonString);
    return parsed.text || parsed.description || JSON.stringify(parsed);
  } catch {
    return jsonString || "";
  }
}

function safeParseLocation(jsonString: string): string {
  try {
    const parsed = JSON.parse(jsonString);
    const parts = [parsed.city, parsed.country].filter(Boolean);
    return parts.length ? parts.join(", ") : JSON.stringify(parsed);
  } catch {
    return jsonString || "";
  }
}

function parseImageUrls(imageUrlJson: string | null | undefined): string[] {
  if (!imageUrlJson) return [];
  try {
    const paths: string[] = JSON.parse(imageUrlJson);
    return paths.map((p) => {
      const filename = p.split("/").pop() || p.split("\\").pop() || p;
      return `${IMAGE_BASE_URL}/${encodeURIComponent(filename)}`;
    });
  } catch {
    return [];
  }
}

function formatDate(iso: string | null | undefined): string {
  if (!iso) return "—";
  return new Date(iso).toLocaleDateString(undefined, {
    year: "numeric", month: "short", day: "numeric",
  });
}

// ─── Job card ───────────────────────────────────────────────────────────────

function JobCard({ job }: { job: JobPost }) {
  const [open, setOpen] = useState(false);
  const fullDescription = safeParseText(job.description);
  const location = safeParseLocation(job.geoLocation);

  return (
    <div className="bg-white rounded-xl border border-gray-100 shadow-sm hover:shadow-md transition-all overflow-hidden">
      {/* Summary row — always visible, click to expand */}
      <button
        type="button"
        onClick={() => setOpen((v) => !v)}
        className="w-full text-left p-5 flex items-start justify-between gap-3 focus:outline-none"
      >
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2 flex-wrap">
            <h3 className="font-bold text-gray-900 text-base truncate">{job.title}</h3>
            <span
              className={`inline-flex items-center gap-1 text-xs font-medium px-2 py-0.5 rounded-full ${
                job.isOpened
                  ? "bg-emerald-50 text-emerald-700"
                  : "bg-red-50 text-red-600"
              }`}
            >
              {job.isOpened ? (
                <><CheckCircle className="h-3 w-3" /> Open</>
              ) : (
                <><XCircle className="h-3 w-3" /> Closed</>
              )}
            </span>
          </div>

          <p className="text-xs text-gray-500 line-clamp-2 mt-1">{fullDescription}</p>

          <div className="flex items-center gap-4 mt-3 text-xs text-gray-400 font-medium flex-wrap">
            <span className="flex items-center gap-1">
              <MapPin className="h-3 w-3" /> {job.city}
            </span>
            {job.salary && (
              <span className="flex items-center gap-0.5 text-emerald-600">
                <DollarSign className="h-3 w-3" />
                {Number(job.salary).toLocaleString()}
              </span>
            )}
          </div>
        </div>

        <span className="text-gray-400 mt-1 shrink-0">
          {open ? <ChevronUp className="h-4 w-4" /> : <ChevronDown className="h-4 w-4" />}
        </span>
      </button>

      {/* Expanded detail panel */}
      {open && (
        <div className="border-t border-gray-100 px-5 pb-5 pt-4 space-y-4 text-sm text-gray-700 bg-gray-50/60">

          {/* Full description */}
          <div>
            <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">Description</p>
            <p className="text-sm text-gray-700 whitespace-pre-line">{fullDescription}</p>
          </div>

          {/* Location from JSONB */}
          <div>
            <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">Location</p>
            <span className="flex items-center gap-1.5 text-sm">
              <Globe className="h-3.5 w-3.5 text-gray-400" />
              {location}
            </span>
          </div>

          {/* Salary */}
          {job.salary && (
            <div>
              <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">Salary</p>
              <span className="flex items-center gap-1 text-emerald-700 font-semibold">
                <DollarSign className="h-3.5 w-3.5" />
                {Number(job.salary).toLocaleString()}
              </span>
            </div>
          )}

          {/* Timestamps */}
          <div className="grid grid-cols-2 gap-3">
            <div>
              <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">Posted</p>
              <span className="flex items-center gap-1.5 text-xs text-gray-600">
                <Calendar className="h-3 w-3 text-gray-400" />
                {formatDate(job.createdAt)}
              </span>
            </div>
            {job.updatedAt && (
              <div>
                <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">Last updated</p>
                <span className="flex items-center gap-1.5 text-xs text-gray-600">
                  <Clock className="h-3 w-3 text-gray-400" />
                  {formatDate(job.updatedAt)}
                </span>
              </div>
            )}
          </div>

          {/* Listing ID */}
          <div>
            <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">Listing ID</p>
            <code className="text-xs text-gray-400 bg-gray-100 px-2 py-0.5 rounded font-mono break-all">{job.id}</code>
          </div>
        </div>
      )}
    </div>
  );
}

// ─── Marketplace card ────────────────────────────────────────────────────────

function MarketplaceCard({ item }: { item: MarketPlaceListing }) {
  const [open, setOpen] = useState(false);
  const images = parseImageUrls(item.imageUrl);
  const fullDescription = safeParseText(item.description);
  const location = safeParseLocation(item.location);

  return (
    <div className="bg-white rounded-xl border border-gray-100 shadow-sm hover:shadow-md transition-all overflow-hidden">
      {/* Image strip */}
      {images.length > 0 ? (
        <div className={`grid ${images.length > 1 ? "grid-cols-2" : "grid-cols-1"} gap-0.5 bg-gray-100`}>
          {images.map((src, idx) => (
            <img
              key={idx}
              src={src}
              alt={`${item.title} image ${idx + 1}`}
              className="w-full h-40 object-cover"
              loading="lazy"
              onError={(e) => { (e.currentTarget as HTMLImageElement).style.display = "none"; }}
            />
          ))}
        </div>
      ) : (
        <div className="flex items-center justify-center h-20 bg-gray-50 border-b border-gray-100">
          <ImageOff className="h-5 w-5 text-gray-300" />
        </div>
      )}

      {/* Summary row */}
      <button
        type="button"
        onClick={() => setOpen((v) => !v)}
        className="w-full text-left p-5 flex items-start justify-between gap-3 focus:outline-none"
      >
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2 flex-wrap">
            <h3 className="font-bold text-gray-900 text-base truncate">{item.title}</h3>
            <span
              className={`inline-flex items-center gap-1 text-xs font-medium px-2 py-0.5 rounded-full ${
                item.isOpen
                  ? "bg-emerald-50 text-emerald-700"
                  : "bg-red-50 text-red-600"
              }`}
            >
              {item.isOpen ? (
                <><CheckCircle className="h-3 w-3" /> Available</>
              ) : (
                <><XCircle className="h-3 w-3" /> Sold</>
              )}
            </span>
          </div>
          <p className="text-xs text-gray-500 line-clamp-2 mt-1">{fullDescription}</p>
          <span className="inline-flex items-center gap-1 text-xs text-gray-400 font-medium mt-3">
            <MapPin className="h-3 w-3" /> {location}
          </span>
        </div>

        <span className="text-gray-400 mt-1 shrink-0">
          {open ? <ChevronUp className="h-4 w-4" /> : <ChevronDown className="h-4 w-4" />}
        </span>
      </button>

      {/* Expanded detail panel */}
      {open && (
        <div className="border-t border-gray-100 px-5 pb-5 pt-4 space-y-4 text-sm text-gray-700 bg-gray-50/60">

          {/* Full description */}
          <div>
            <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">Description</p>
            <p className="text-sm text-gray-700 whitespace-pre-line">{fullDescription}</p>
          </div>

          {/* Location */}
          <div>
            <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">Location</p>
            <span className="flex items-center gap-1.5 text-sm">
              <Globe className="h-3.5 w-3.5 text-gray-400" />
              {location}
            </span>
          </div>

          {/* All images (full size if expanded) */}
          {images.length > 0 && (
            <div>
              <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-2">
                Images ({images.length})
              </p>
              <div className="grid grid-cols-2 gap-2">
                {images.map((src, idx) => (
                  <a key={idx} href={src} target="_blank" rel="noopener noreferrer">
                    <img
                      src={src}
                      alt={`${item.title} full ${idx + 1}`}
                      className="w-full rounded-lg object-cover h-32 hover:opacity-90 transition-opacity"
                      loading="lazy"
                      onError={(e) => { (e.currentTarget as HTMLImageElement).style.display = "none"; }}
                    />
                  </a>
                ))}
              </div>
            </div>
          )}

          {/* Timestamps */}
          <div className="grid grid-cols-2 gap-3">
            <div>
              <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">Listed</p>
              <span className="flex items-center gap-1.5 text-xs text-gray-600">
                <Calendar className="h-3 w-3 text-gray-400" />
                {formatDate(item.createdAt)}
              </span>
            </div>
            {item.updatedAt && (
              <div>
                <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">Last updated</p>
                <span className="flex items-center gap-1.5 text-xs text-gray-600">
                  <Clock className="h-3 w-3 text-gray-400" />
                  {formatDate(item.updatedAt)}
                </span>
              </div>
            )}
          </div>

          {/* Listing ID */}
          <div>
            <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1">Listing ID</p>
            <code className="text-xs text-gray-400 bg-gray-100 px-2 py-0.5 rounded font-mono break-all">{item.id}</code>
          </div>
        </div>
      )}
    </div>
  );
}

// ─── Page ────────────────────────────────────────────────────────────────────

export default function Home() {
  const [activeSearch, setActiveSearch] = useState("");

  const {
    data,
    isLoading,
    isError,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useUnifiedSearch(activeSearch);

  const allJobs = data?.pages.flatMap((page) => page.jobPosts) || [];
  const allMarketplace = data?.pages.flatMap((page) => page.marketplaceListings) || [];

  const totalJobs = data?.pages[data.pages.length - 1]?.totalJobsFound || 0;
  const totalMarketplace = data?.pages[data.pages.length - 1]?.totalMarketplaceFound || 0;

  return (
    <main className="min-h-screen bg-gray-50/50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-5xl mx-auto space-y-10">

        <div className="text-center space-y-2">
          <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight">Infinite Finder</h1>
          <p className="text-sm text-gray-500">Retrieving dual table aggregates 10 by 10 sequentially.</p>
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

              {/* JOBS COLUMN */}
              <div className="space-y-4">
                <div className="flex items-center space-x-2 pb-2 border-b border-gray-200">
                  <Briefcase className="h-4 w-4 text-gray-700" />
                  <h2 className="font-bold text-gray-800 text-lg">Job Openings ({totalJobs})</h2>
                </div>
                {allJobs.map((job) => <JobCard key={job.id} job={job} />)}
                {allJobs.length === 0 && (
                  <p className="text-sm text-gray-400 py-2">No matching jobs.</p>
                )}
              </div>

              {/* MARKETPLACE COLUMN */}
              <div className="space-y-4">
                <div className="flex items-center space-x-2 pb-2 border-b border-gray-200">
                  <ShoppingBag className="h-4 w-4 text-gray-700" />
                  <h2 className="font-bold text-gray-800 text-lg">Marketplace ({totalMarketplace})</h2>
                </div>
                {allMarketplace.map((item) => <MarketplaceCard key={item.id} item={item} />)}
                {allMarketplace.length === 0 && (
                  <p className="text-sm text-gray-400 py-2">No matching items.</p>
                )}
              </div>

            </div>

            {/* LOAD MORE */}
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
