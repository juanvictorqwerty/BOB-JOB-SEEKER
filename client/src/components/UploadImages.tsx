// components/marketplace/ImageUpload.tsx
'use client';

import React, { useRef } from 'react';
import { UploadCloud, X, Image as ImageIcon } from 'lucide-react';

interface ImageUploadProps {
    selectedFiles: File[];
    onChange: (files: File[]) => void;
}

export default function ImageUpload({ selectedFiles, onChange }: ImageUploadProps) {
    const fileInputRef = useRef<HTMLInputElement>(null);

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            const filesArray = Array.from(e.target.files);
            validateAndAddFiles(filesArray);
        }
    };

    const validateAndAddFiles = (files: File[]) => {
        const updatedFiles = [...selectedFiles, ...files].slice(0, 2); // Max 2 images

        // Filter out files larger than 5MB
        const validFiles = updatedFiles.filter(file => {
            if (file.size > 5 * 1024 * 1024) {
                alert(`File ${file.name} is too large. Maximum size is 5MB.`);
                return false;
            }
            return true;
        });

        onChange(validFiles);
    };

    const removeFile = (index: number) => {
        onChange(selectedFiles.filter((_, i) => i !== index));
    };

    return (
        <div className="space-y-4">
            <label className="block text-sm font-semibold text-gray-700">Listing Images (Max 2, up to 5MB each)</label>

            {selectedFiles.length < 2 && (
                <div
                    onClick={() => fileInputRef.current?.click()}
                    className="border-2 border-dashed border-gray-200 hover:border-blue-500 bg-gray-50/50 rounded-xl p-8 text-center cursor-pointer transition-all duration-200 group"
                >
                    <input
                        type="file"
                        ref={fileInputRef}
                        onChange={handleFileChange}
                        multiple
                        accept="image/*"
                        className="hidden"
                    />
                    <UploadCloud className="mx-auto h-10 w-10 text-gray-400 group-hover:text-blue-500 transition-colors" />
                    <p className="mt-2 text-sm font-medium text-gray-600">
                        Click to upload or drag and drop
                    </p>
                    <p className="text-xs text-gray-400 mt-1">PNG, JPG, or WEBP up to 5MB</p>
                </div>
            )}

            {selectedFiles.length > 0 && (
                <div className="grid grid-cols-2 gap-4">
                    {selectedFiles.map((file, index) => (
                        <div key={index} className="relative border border-gray-100 rounded-xl p-3 bg-white shadow-sm flex items-center space-x-3">
                            <div className="p-2 bg-blue-50 rounded-lg text-blue-600">
                                <ImageIcon className="h-6 w-6" />
                            </div>
                            <div className="flex-1 min-w-0">
                                <p className="text-sm font-medium text-gray-700 truncate">{file.name}</p>
                                <p className="text-xs text-gray-400">{(file.size / (1024 * 1024)).toFixed(2)} MB</p>
                            </div>
                            <button
                                type="button"
                                onClick={() => removeFile(index)}
                                className="p-1 text-gray-400 hover:text-red-500 hover:bg-gray-50 rounded-lg transition-colors"
                            >
                                <X className="h-4 w-4" />
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}