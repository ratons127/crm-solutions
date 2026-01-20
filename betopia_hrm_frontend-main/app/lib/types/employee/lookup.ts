import { DBModel, Simplify } from '..';

export type LookupSetupEntry = Simplify<
  DBModel & {
    name: string;
    status: boolean;
  }
>;

export type LookupSetupEntryCreate = Simplify<
  Omit<LookupSetupEntry, keyof DBModel>
>;
export type LookupSetupEntryUpdate = Simplify<Partial<LookupSetupEntryCreate>>;

// details
export type LookupSetupEntryDetails = DBModel & {
  setup?: LookupSetupEntry;
  name: string;
  details: string;
  status: boolean;
};

export type LookupSetupEntryDetailsCreate = Simplify<
  Omit<LookupSetupEntryDetails, keyof DBModel | 'lookupSetupEntry'> & {
    lookupSetupEntryId: number | string;
  }
>;
export type LookupSetupEntryDetailsUpdate = Simplify<
  Partial<LookupSetupEntryDetailsCreate>
>;
