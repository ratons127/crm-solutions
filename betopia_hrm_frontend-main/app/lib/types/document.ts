import { DBModel } from '.';

export interface DocumentType extends DBModel {
  name: string;
  category: string;
  description: string;
}

export interface DocumentTypeListType extends DBModel {
  name: string;
}

export interface EmployeeDocumentVersion extends DBModel {
  versionNo: number;
  filePath: string;
  uploadedAt: string;
  user: number;
  current: boolean;
}

export interface EmployeeDocument extends DBModel {
  employee: number;
  documentType: DocumentType;
  filePath: string;
  issueDate: string;
  expiryDate: string;
  documentStatus: 'ACTIVE' | 'INACTIVE';
  status: boolean;
  currentVersion: number;
  employeeDocumentVersions: EmployeeDocumentVersion[];
}

// Form Types
export interface DocumentFormValues {
  employeeId: string;
  documentTypeId: string;
  filePath: File | null;
  issueDate: Date | null;
  expiryDate: Date | null;
  status: boolean;
}

export interface DocumentPayloadData {
  employeeId: number;
  documentTypeId: number;
  documentStatus: 'ACTIVE' | 'INACTIVE';
  status: boolean;
  issueDate?: string;
  expiryDate?: string;
  employeeDocumentVersions?: Array<{ userId: number }>;
}

export interface EmployeeDocumentListResponse {
  data: EmployeeDocument[];
  message: string;
  success: boolean;
  status: number;
}

export interface PaginatedEmployeeDocumentResponse {
  data: EmployeeDocument[];
  meta: {
    total: number;
    perPage: number;
    currentPage: number;
    lastPage: number;
    firstPage: number;
    firstPageUrl: string;
    lastPageUrl: string;
    nextPageUrl: string | null;
    previousPageUrl: string | null;
  };
  message: string;
  success: boolean;
  status: number;
}

// API Response Types
export interface ApiResponse {
  message: string;
  success: boolean;
  status: number;
}

export interface CreateUpdateDocumentResponse extends ApiResponse {
  data?: any;
}

export interface DeleteDocumentResponse extends ApiResponse {
  data?: null;
}

// Document Type Form Types
export interface DocumentTypeFormValues {
  name: string;
  category: string;
  description?: string;
}

// Document Type Response Types
export interface PaginatedDocumentTypeResponse {
  data: DocumentType[];
  meta: {
    total: number;
    perPage: number;
    currentPage: number;
    lastPage: number;
    firstPage: number;
    firstPageUrl: string;
    lastPageUrl: string;
    nextPageUrl: string | null;
    previousPageUrl: string | null;
  };
  message: string;
  success: boolean;
  status: number;
}

export interface CreateUpdateDocumentTypeResponse extends ApiResponse {
  data?: DocumentType;
}

export interface DeleteDocumentTypeResponse extends ApiResponse {
  data?: null;
}
