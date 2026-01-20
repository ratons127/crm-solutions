import { useAppSelector } from '@/lib/hooks';
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
export type QualificationMethod = {
  title: string;
  id: number;
};
type InitialState = {
  qualificationMethods: QualificationMethod[];
};

const initialState: InitialState = {
  qualificationMethods: [],
};

const slice = createSlice({
  name: 'employee_qualification_setup',
  initialState,
  reducers: {
    // add item
    addMethod: (
      state,
      action: PayloadAction<Omit<QualificationMethod, 'id'>>
    ) => {
      state.qualificationMethods = state.qualificationMethods.concat({
        ...action.payload,
        id: state.qualificationMethods?.at(-1)?.id ?? 0 + 1,
      });
    },
    // remove item
    removeMethod: (state, action: PayloadAction<number>) => {
      state.qualificationMethods = state.qualificationMethods.filter(
        x => x.id !== action.payload
      );
    },
    updateMethod: (
      state,
      action: PayloadAction<{
        id: number;
        data: Omit<QualificationMethod, 'id'>;
      }>
    ) => {
      state.qualificationMethods = state.qualificationMethods.map(x => {
        if (x.id !== action.payload.id) {
          return {
            ...action.payload.data,
            id: action.payload.id,
          };
        }
        return x;
      });
    },
  },
});

export const employeeQualificationSetupReducer = slice.reducer;
export const { addMethod, removeMethod, updateMethod } = slice.actions;
export const useEmployeeQualificationSetup = () =>
  useAppSelector(state => state.employeeQualificationSetup);
