import { combineReducers, configureStore } from '@reduxjs/toolkit';

import { departmentReducer } from '@/lib/features/companyConfig/departmentSlice';
import teamReducer from '@/lib/features/companyConfig/teamSlice';
import { leaveTypeRuleAPI } from '../services/api/leave/leaveTypeRuleAPI';
import { appReducer } from './features/app/appSlice';
import authReducer from './features/auth/authSlice';
import businessUnitReducer from './features/companyConfig/businessUnitSlice';
import companiesReducer from './features/companyConfig/companyConfigSlice';
import workplaceGroupReducer from './features/companyConfig/workplaceGroupSlice';
import workplaceReducer from './features/companyConfig/workplaceSlice';
import employeesReducer from './features/employees/employeeSlice';
import { employeeProfileReducer } from './features/employees/profile/employeeProfileSlice';
import { employeeQualificationSetupReducer } from './features/employees/setup/qualificationSlice';
import transferReducer from './features/employees/transfer/transferSlice';
import designationReducer from './features/employees/workstructure/designationSlice';
import gradeReducer from './features/employees/workstructure/gradeSlice';
import employeesProfileReducer from './features/employeesProfile/employeeProfileSlice';
import { leaveBalanceEmployeeReducer } from './features/leave/balanceEmployee/balanceEmployeeSlice';
import leaveApplyReducer from './features/leave/leaveApply/leaveApplySlice';
import { leaveGroupReducer } from './features/leave/leaveGroup/leaveGroupeSlice';
import { leaveYearReducer } from './features/leave/leaveYear/leaveYearSlice';
import { leavePolicyReducer } from './features/leave/policy/leavePolicySlice';
import leaveTypeReducer from './features/leave/type/leaveTypeSlice';
import rolesReducer from './features/role/roleSlice';
import usersReducer from './features/user/userSlice';

const rootReducer = combineReducers({
  app: appReducer,
  auth: authReducer,
  employees: employeesReducer,
  leavePolicies: leavePolicyReducer,
  leaveTypes: leaveTypeReducer,
  leaveBalanceEmployee: leaveBalanceEmployeeReducer,
  leaveGroup: leaveGroupReducer,
  leaveYear: leaveYearReducer,
  users: usersReducer,
  roles: rolesReducer,
  leaveApply: leaveApplyReducer,
  companies: companiesReducer,
  businessUnit: businessUnitReducer,
  workplaces: workplaceReducer,
  workplaceGroup: workplaceGroupReducer,
  departments: departmentReducer,
  team: teamReducer,
  employeesProfile: employeesProfileReducer,
  employeeProfile: employeeProfileReducer,
  employeeQualificationSetup: employeeQualificationSetupReducer,
  grade: gradeReducer,
  designation: designationReducer,
  transfer: transferReducer,

  // api
  [leaveTypeRuleAPI.reducerPath]: leaveTypeRuleAPI.reducer,
});

export type RootState = ReturnType<typeof rootReducer>;

// Hydration from encrypted storage is handled client-side by a provider.
function loadFromLocalStorage(): Partial<RootState> | undefined {
  return undefined;
}
export function makeStore(preloadedState?: Partial<RootState>) {
  return configureStore({
    reducer: rootReducer,
    preloadedState: preloadedState ?? loadFromLocalStorage(),
    middleware: getDefaultMiddleware => {
      return getDefaultMiddleware().concat(leaveTypeRuleAPI.middleware);
    },
  });
}

const store = makeStore();

export type AppStore = ReturnType<typeof makeStore>;
export type AppDispatch = AppStore['dispatch'];

export default store;

