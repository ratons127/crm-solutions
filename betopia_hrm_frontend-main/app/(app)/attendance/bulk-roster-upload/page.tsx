'use client';

import React, { useState, DragEvent, ChangeEvent } from 'react';
import { Button, Group } from '@mantine/core';
import { MdOutlineFileDownload } from 'react-icons/md';

import { AiOutlineInfoCircle } from 'react-icons/ai';
import { BsCheckCircle } from 'react-icons/bs';
import Breadcrumbs from '@/components/common/Breadcrumbs';
import { LuUpload } from 'react-icons/lu';

const BulkRosterUploadPage = () => {
  const [file, setFile] = useState<File | null>(null);
  const [isDragging, setIsDragging] = useState(false);

  const handleDragOver = (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    setIsDragging(true);
  };

  const handleDragLeave = () => setIsDragging(false);

  const handleDrop = (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    setIsDragging(false);
    if (e.dataTransfer.files.length > 0) {
      setFile(e.dataTransfer.files[0]);
    }
  };

  const handleFileSelect = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setFile(e.target.files[0]);
    }
  };

  return (
    <div>
      {/* ======= Page Header ======= */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-800 mb-2">
            Bulk Roster Upload
          </h1>
          <Breadcrumbs />
        </div>

        <div className="flex justify-start md:justify-end items-center">
          <Group>
            <Button
              leftSection={<MdOutlineFileDownload size={16} color="#1E293B" />}
              variant="outline"
              radius="md"
              styles={{
                root: { borderColor: '#CBD5E1' },
                label: { color: '#1E293B' },
              }}
            >
              Download Template
            </Button>
          </Group>
        </div>
      </div>

      {/* ======= Upload Section ======= */}
      <div className="bg-white p-6 rounded-lg shadow-sm">
        <div>
          <div className="flex items-center gap-2 mb-2">
            <MdOutlineFileDownload size={20} color="#1E293B" />
            <span className="text-base text-gray-800 font-medium">
              Upload Roster File
            </span>
          </div>
          <p className="text-sm text-gray-600">
            Upload a CSV or Excel file containing roster assignments
          </p>
        </div>

        {/* ======= Drag & Drop Box ======= */}
        <div
          onDragOver={handleDragOver}
          onDragLeave={handleDragLeave}
          onDrop={handleDrop}
          className={`border-2 flex flex-col items-center justify-center border-dashed rounded-lg px-6 py-8 my-6 text-center transition-all duration-300 ${
            isDragging
              ? 'border-blue-400 bg-blue-50'
              : 'border-gray-300 bg-gray-50'
          }`}
        >
          <div className="w-16 h-16 bg-orange-100 rounded-full flex items-center justify-center mb-3">
            <LuUpload className="text-3xl text-orange-600" />
          </div>
          {file ? (
            <p className="text-gray-800 font-medium">{file.name}</p>
          ) : (
            <>
              <p className="text-gray-800 font-medium mb-2">
                Drag and drop your file here
              </p>
              <p className="text-gray-500 text-sm mb-3">or</p>
              <label className="inline-block bg-orange-500 text-white px-4 py-2 rounded-md cursor-pointer hover:bg-orange-600 transition-colors">
                Browse Files
                <input
                  type="file"
                  accept=".csv,.xlsx,.xls"
                  hidden
                  onChange={handleFileSelect}
                />
              </label>
              <p className="text-gray-500 text-xs mt-3">
                Supported formats: CSV, Excel (.xlsx, .xls) • Max file size: 10MB
              </p>
            </>
          )}
        </div>

        {/* ======= Info Section ======= */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {/* Required Columns */}
          <div className="border border-blue-200 bg-blue-50 rounded-md p-4">
            <div className="flex items-center gap-2 mb-2 text-blue-700 font-semibold text-sm">
              <AiOutlineInfoCircle size={18} /> Required Columns
            </div>
            <ul className="text-xs text-gray-700 space-y-1 list-disc list-inside">
              <li>Employee ID (required)</li>
              <li>Employee Name (required)</li>
              <li>Date (YYYY-MM-DD format)</li>
              <li>Shift Code (required)</li>
              <li>Shift Name (optional)</li>
              <li>Remarks (optional)</li>
            </ul>
          </div>

          {/* File Guidelines */}
          <div className="border border-green-200 bg-green-50 rounded-md p-4">
            <div className="flex items-center gap-2 mb-2 text-green-700 font-semibold text-sm">
              <BsCheckCircle size={18} /> File Guidelines
            </div>
            <ul className="text-xs text-gray-700 space-y-1 list-disc list-inside">
              <li>Use CSV or Excel format</li>
              <li>Include header row</li>
              <li>Maximum 1000 rows per file</li>
              <li>Employee IDs must exist</li>
              <li>Valid date format required</li>
              <li>Shift codes must be defined</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BulkRosterUploadPage;
