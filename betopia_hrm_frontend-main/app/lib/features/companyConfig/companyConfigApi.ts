// lib/features/company/companyConfigApi.ts
import apiClient from '../../../services/apiClient';

/* ---------------- Types ---------------- */
export type CompanyRow = {
  id: number;
  name: string;
  shortName?: string | null;
  code?: string | null;
  phone?: string | null;
  email?: string | null;
  websiteUrl?: string | null;
  description?: string | null;
  countryId?: number | string | null;
  divisionId?: number | string | null;
  districtId?: number | string | null;
  thana?: number | string | null;
  postOffice?: number | string | null;
  address?: string | null;
  image?: string | null;
  status: boolean;
  currency?: string | null;
  timeZone?: string | null;
  /** returned after upload */
  logoUrl?: string | null;
};

export type Paginated<T> = {
  data: T[];
  links?: {
    first?: string | null;
    last?: string | null;
    prev?: string | null;
    next?: string | null;
  } | null;
  meta?: {
    currentPage?: number;
    from?: number | null;
    lastPage?: number;
    path?: string;
    perPage?: number;
    to?: number | null;
    total?: number;
  } | null;
};

export type ListParams = {
  page?: number;
  size?: number;
  sort?: string;
  q?: string;
};

/* ---------------- Helpers ---------------- */
function toQuery(params: Record<string, any>) {
  const s = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== null && v !== '') s.append(k, String(v));
  });
  return s.toString();
}

function asNumOrNull(v: unknown) {
  if (v === '' || v === undefined || v === null) return null;
  const n = Number(v);
  return Number.isFinite(n) ? n : null;
}

/* ---------------- Reads ---------------- */
export async function listCompanies(params: ListParams = {}) {
  const query = toQuery({
    page: params.page ?? 1,
    size: params.size ?? 10,
    sort: params.sort,
    q: params.q,
  });
  const { data } = await apiClient.get<Paginated<CompanyRow>>(
    `/companies?${query}`
  );
  return data;
}

export async function showCompany(id: number | string) {
  const { data } = await apiClient.get<CompanyRow>(`/companies/${id}`);
  return data;
}

/* ---------------- Write payloads ---------------- */
export type CreateCompanyPayload = {
  companyLogo?: File | null;
  companyName: string;
  companyShortName?: string | null;
  companyCode?: string | null;
  phone?: string | null;
  companyEmail?: string | null;
  companyWebsite?: string | null;
  companyDescription?: string | null;
  countryId?: number | string | null;
  divisionId?: number | string | null;
  districtId?: number | string | null;
  policeStationId?: number | string | null;
  postOfficeId?: number | string | null;
  address?: string | null;
  status?: boolean;
  currency: string;
  timeZone: string;
  image?: string | null;
};

export type UpdateCompanyPayload = CreateCompanyPayload & {
  id: number | string;
};

/* ---------------- Payload mapper ---------------- */
function toApiBody(p: Omit<CreateCompanyPayload, 'companyLogo'>) {
  return {
    name: p.companyName,
    shortName: p.companyShortName ?? null,
    code: p.companyCode ?? null,
    phone: p.phone ?? null,
    email: p.companyEmail ?? null,
    websiteUrl: p.companyWebsite ?? null,
    description: p.companyDescription ?? null,
    countryId: asNumOrNull(p.countryId),
    divisionId: asNumOrNull(p.divisionId),
    districtId: asNumOrNull(p.districtId),
    thana: asNumOrNull(p.policeStationId),
    postOffice: asNumOrNull(p.postOfficeId),
    address: p.address ?? null,
    image: p.image ?? null,
    currency: p.currency ? p.currency.toUpperCase() : null,
    timeZone: p.timeZone ?? null,
    status: typeof p.status === 'boolean' ? p.status : true,
  };
}

/* ---------------- Image upload ---------------- */
export async function uploadCompanyImage(companyId: number | string, file: File) {
  const form = new FormData();
  form.append('file', file, file.name); // include filename

  const { data } = await apiClient.post(
    `/companies/${companyId}/upload-image`,
    form,
    {
      // If your axios instance sets JSON globally, override here:
      headers: { 'Content-Type': 'multipart/form-data' },
    }
  );

  const logoUrl = (data as any)?.logoUrl ?? (data as any)?.data?.logoUrl ?? null;
  return { logoUrl };
}


/* ---------------- Create ---------------- */
export async function createCompany(payload: CreateCompanyPayload) {
  const { companyLogo, ...rest } = payload;

  // 1️⃣ Create entity (JSON)
  const body = toApiBody(rest);
  const createdResp = await apiClient.post(`/companies`, body);

  const created: CompanyRow = (createdResp.data &&
    (createdResp.data.data ?? createdResp.data)) as CompanyRow;

  // 2️⃣ Upload image if provided
  if (companyLogo instanceof File) {
    try {
      await uploadCompanyImage(created.id, companyLogo);
    } catch (e) {
      console.warn('Logo upload failed', e);
    }
  }

  // 3️⃣ Return fresh data
  return await showCompany(created.id);
}

/* ---------------- Update ---------------- */
export async function updateCompany(payload: UpdateCompanyPayload) {
  const { id, companyLogo, ...rest } = payload;

  // 1️⃣ Update entity
  const body = toApiBody(rest);
  await apiClient.put(`/companies/${id}`, body);

  // 2️⃣ Upload image if provided
  if (companyLogo instanceof File) {
    try {
      await uploadCompanyImage(id, companyLogo);
    } catch (e) {
      console.warn('Logo upload failed', e);
    }
  }

  // 3️⃣ Return fresh record
  return await showCompany(id);
}

/* ---------------- Delete ---------------- */
export async function deleteCompany(id: number | string) {
  const { data } = await apiClient.delete(`/companies/${id}`);
  return data;
}

/* ---------------- All Zones / All Currencies ---------------- */
export type SelectOption = { value: string; label: string };

function toOptions(list: any[]): SelectOption[] {
  return (list ?? []).map((it: any) => {
    if (typeof it === 'string') return { value: it, label: it };

    const code =
      it.code ?? it.currencyCode ?? it.zone ?? it.zoneId ?? it.id ?? it.value ?? it.name;
    const label =
      it.label ??
      it.displayName ??
      it.name ??
      it.symbol ??
      it.code ??
      it.currencyCode ??
      String(code ?? '');

    const v = String(code ?? label ?? '');
    return { value: v, label: String(label ?? v) };
  });
}

function unwrapList(payload: unknown): any[] {
  if (Array.isArray(payload)) return payload;
  const maybe = (payload as any)?.data;
  return Array.isArray(maybe) ? maybe : [];
}

/** GET /v1/companies/all-zones */
export async function getAllZones(): Promise<SelectOption[]> {
  const resp = await apiClient.get(`/companies/all-zones`);
  return toOptions(unwrapList(resp.data));
}

/** GET /v1/companies/all-currencies */
export async function getAllCurrencies(): Promise<SelectOption[]> {
  const resp = await apiClient.get(`/companies/all-currencies`);
  return toOptions(unwrapList(resp.data));
}
