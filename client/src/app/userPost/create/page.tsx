// app/marketplace/new/page.tsx
'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { ShoppingBag, MapPin, FileText, Plus, Tag } from 'lucide-react';
import ImageUpload from '@/components/UploadImages';

export default function NewMarketplacePost() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [tagInput, setTagInput] = useState('');

    const [form, setForm] = useState({
        title: '',
        city: '',
        isRemote: false,
        textDescription: '',
        images: [] as File[]
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        try {
            // 1. Structure JSON payloads precisely to match backend specifications
            const locationJson = JSON.stringify({
                city: form.city,
                remote: form.isRemote
            });

            const descriptionJson = JSON.stringify({
                text: form.textDescription,
            });

            // 2. Build multi-part form data array stream
            const formData = new FormData();

            // Inject your standard DTO data wrapper stringified block
            formData.append('data', new Blob([JSON.stringify({
                title: form.title,
                location: locationJson,
                description: descriptionJson
            })], { type: 'application/json' }));

            // Attach file binaries
            form.images.forEach((image) => {
                formData.append('images', image);
            });

            // 3. Dispatch to Spring Boot Endpoint
            const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE}/add/marketplace`, {
                method: 'POST',
                credentials: 'include',
                body: formData, // Browser auto-creates boundary parameters
            });

            if (!response.ok) throw new Error('Failed to publish listing');

            router.push('/');
        } catch (error) {
            console.error(error);
            alert('Something went wrong checking out your submission.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50/50 py-12 px-4 sm:px-6 lg:px-8 font-sans">
            <div className="max-w-2xl mx-auto bg-white rounded-2xl border border-gray-100 shadow-sm overflow-hidden">

                {/* Banner header */}
                <div className="px-8 py-6 border-b border-gray-100 bg-white flex items-center space-x-4">
                    <div className="p-3 bg-blue-50 text-blue-600 rounded-xl">
                        <ShoppingBag className="h-6 w-6" />
                    </div>
                    <div>
                        <h1 className="text-xl font-bold text-gray-900">Tell us what you can do</h1>
                        <p className="text-sm text-gray-500">Yours skills are needed somewhere, let's help you find the perfect match  </p>
                    </div>
                </div>

                {/* Form Body */}
                <form onSubmit={handleSubmit} className="p-8 space-y-6">

                    {/* Title */}
                    <div className="space-y-2">
                        <label className="block text-sm font-semibold text-gray-700">Your function</label>
                        <input
                            type="text"
                            required
                            placeholder="e.g.,Autocad expert"
                            value={form.title}
                            onChange={e => setForm({ ...form, title: e.target.value })}
                            className="w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all text-sm text-gray-800"
                        />
                    </div>

                    {/* Location Group */}
                    <div className="bg-gray-50/50 border border-gray-100 rounded-xl p-5 space-y-4">
                        <div className="flex items-center space-x-2 text-gray-700 font-semibold text-sm">
                            <MapPin className="h-4 w-4 text-gray-400" />
                            <span>Location Context</span>
                        </div>

                        <div className="grid grid-cols-2 gap-4">
                            <div className="space-y-1">
                                <label className="text-xs text-gray-500 font-medium">City</label>
                                <input
                                    type="text" required placeholder="Paris" value={form.city}
                                    onChange={e => setForm({ ...form, city: e.target.value })}
                                    className="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:ring-1 focus:ring-blue-500 focus:outline-none"
                                />
                            </div>
                        </div>

                        <div className="flex items-center space-x-3 pt-2">
                            <input
                                type="checkbox" id="remote" checked={form.isRemote}
                                onChange={e => setForm({ ...form, isRemote: e.target.checked })}
                                className="h-4 w-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                            />
                            <label htmlFor="remote" className="text-xs font-medium text-gray-600 cursor-pointer">
                                You are available for remote work
                            </label>
                        </div>
                    </div>

                    {/* Structured Description Field */}
                    <div className="space-y-2">
                        <div className="flex items-center space-x-2 text-sm font-semibold text-gray-700">
                            <FileText className="h-4 w-4 text-gray-400" />
                            <label>Tell us more about what you can do</label>
                        </div>
                        <textarea
                            rows={4}
                            required
                            placeholder="I created a new language..."
                            value={form.textDescription}
                            onChange={e => setForm({ ...form, textDescription: e.target.value })}
                            className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all text-sm text-gray-800 resize-none"
                        />
                    </div>

                    {/* Upload Component Hook */}
                    <ImageUpload
                        selectedFiles={form.images}
                        onChange={(files) => setForm({ ...form, images: files })}
                    />

                    {/* Action Trigger Buttons */}
                    <div className="pt-4 border-t border-gray-100 flex items-center justify-end space-x-3">
                        <button
                            type="button"
                            disabled={loading}
                            onClick={() => router.back()}
                            className="px-5 py-2.5 border border-gray-200 rounded-xl text-sm font-medium text-gray-600 hover:bg-gray-50 transition-colors disabled:opacity-50"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={loading}
                            className="px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-sm font-semibold transition-colors shadow-sm disabled:opacity-50 flex items-center"
                        >
                            {loading ? 'Publishing...' : 'Publish Listing'}
                        </button>
                    </div>

                </form>
            </div>
        </div>
    );
}