import { RootState } from '../../store';
import { Role } from '../../types/auth';

export const selectCurrentUser = (state: RootState) => state.auth.user;
export const selectIsAuthenticated = (state: RootState) => !!state.auth.token;

export const selectHasRole =
  (role: Role) =>
  (state: RootState): boolean => {
    const user = state.auth.user;
    return user ? user.roles.includes(role) : false;
  };

export const selectHasAnyRole =
  (roles: Role[]) =>
  (state: RootState): boolean => {
    const user = state.auth.user;
    if (!user) return false;
    return roles.some(role => user.roles.includes(role));
  };
