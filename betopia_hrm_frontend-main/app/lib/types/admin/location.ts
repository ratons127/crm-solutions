import { DBModel, Simplify } from '..';

export type CountryCreate = {
  name: string;
  code: string;
  region: string;
};
export type CountryUpdate = Simplify<Partial<CountryCreate>>;
export type Country = DBModel & CountryCreate;

type CountryType = {
  id?: number | string;
};

export type LocationCreate = {
  countryId: number | string;
  parentId: number | string | null;
  name: string;
  type: 'COUNTRY' | 'DIVISION' | 'DISTRICT' | 'POLICE_STATION' | 'POST_OFFICE';
  geoHash: string;
  status: boolean;
  country?: CountryType;
};

export type LocationUpdate = Simplify<Partial<LocationCreate>>;

export type Location = DBModel & LocationCreate;
