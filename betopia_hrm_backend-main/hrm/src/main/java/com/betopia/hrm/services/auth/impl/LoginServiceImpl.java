package com.betopia.hrm.services.auth.impl;

import com.betopia.hrm.domain.auth.login.LoginRequest;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.users.entity.*;
import com.betopia.hrm.domain.users.exception.user.InvalidPasswordException;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.MenuRepository;
import com.betopia.hrm.domain.users.repository.TokenRepository;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.domain.users.request.TokenRequest;
import com.betopia.hrm.domain.base.response.AuthResponse;
import com.betopia.hrm.services.auth.JwtService;
import com.betopia.hrm.services.auth.LoginService;
import com.betopia.hrm.services.users.menu.MenuService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;

    private final MenuService menuService;

    private final MenuRepository menuRepository;

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private  static  final Long EXPIRATION_TIME = 86400000L;

    private final EmployeeRepository employeeRepository;

    public LoginServiceImpl(
            UserRepository userRepository, MenuService menuService, MenuRepository menuRepository,
            TokenRepository tokenRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            EmployeeRepository employeeRepository
    ) {
        this.userRepository = userRepository;
        this.menuService = menuService;
        this.menuRepository = menuRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
    }

//    @Override
//    public AuthResponse login(LoginRequest request) {
//
//        AuthResponse response = new AuthResponse();
//
//        try{
//
////            Long employeeId = null;
////            Long companyId = null;
//
//            // Step 1: Find user by email, phone, or employeeSerialId
//            User user = resolveUserByIdentifier(request.identifier());
//            if (user == null) {
//                throw new UsernameNotFoundException("User not found with given identifier");
//            }
//
//            // Step 2: Check password manually (since authenticationManager not used)
//            if (!passwordEncoder.matches(request.password(), user.getPassword())) {
//                throw new BadCredentialsException("Invalid username or password");
//            }
//
//            // Step 3: Handle user type logic
////            String userType = user.getUserType(); // e.g., "SuperAdmin", "Admin", "Employee"
////            if ("SuperAdmin".equalsIgnoreCase(userType) || "Admin".equalsIgnoreCase(userType)) {
////                // Admin users don't need employee/company validation
////            } else {
////                Integer employeeSerialId = user.getEmployeeSerialId();
////                if (employeeSerialId != null) {
////                    Optional<Employee> employeeOpt = employeeRepository.findByEmployeeSerialId(employeeSerialId);
////                    if (employeeOpt.isPresent()) {
////                        Employee employee = employeeOpt.get();
////                        employeeId = employee.getId();
////                        // You can extract company info here if needed:
////                        companyId = employee.getCompany().getId();
////                    }
////                }
////            }
//
//            // Step 4: Fetch permissions from roles
//            List<Permission> permissions = new ArrayList<>();
//            if (user.getRoles() != null && user.getRoles().getPermissions() != null) {
//                permissions.addAll(user.getRoles().getPermissions());
//            }
//
//            List<Long> permissionIds = permissions.stream()
//                    .map(Permission::getId)
//                    .toList();
//
//            // Step 5: Generate JWT tokens
//            String jwtToken = jwtService.generateToken(user);
//            String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
//
//            if (user.getId() > 0) {
//                response.setUser(user);
//
//                Map<String, Object> roleUpdate = new HashMap<>();
//                roleUpdate.put("id", user.getRoles().getId());
//                roleUpdate.put("name", user.getRoles().getName());
//                roleUpdate.put("created_at", user.getRoles().getCreatedDate());
//                roleUpdate.put("updated_at", user.getRoles().getLastModifiedDate());
//
//                response.setRole(roleUpdate);
//                response.setPermissions(permissions);
//
//                //List<Menu> allMenus = menuService.getAllMenus();
////                List<Menu> filteredMenus = allMenus.stream()
////                        .filter(menu -> menu.getPermission() != null &&
////                                permissionIds.contains(menu.getPermission().getId()))
////                        .collect(Collectors.toList());
//
////                List<Menu> menuTree = buildMenuTree(filteredMenus, null);
////                response.setMenus(menuTree);
//
//                response.setMessage("Login successful");
//                response.setStatusCode(HttpStatus.OK.value());
//                response.setToken(jwtToken);
//                response.setRefreshToken(refreshToken);
//                response.setExpiration(EXPIRATION_TIME);
//                //response.setEmployeeId(employeeId);
//                response.setEmployeeSerialId(user.getEmployeeSerialId());
//                //response.setCompanyId(companyId);
//            }
//
//        } catch (UsernameNotFoundException | ResourceNotFoundException e) {
//            throw e;
//        } catch (BadCredentialsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new RuntimeException("Something went wrong during login", e);
//        }
//
//        return response;
//    }

    @Override
    public AuthResponse login(LoginRequest request) {
        AuthResponse response = new AuthResponse();

        try {
            // 1️⃣ Find user by email, phone, or employeeSerialId
            User user = resolveUserByIdentifier(request.identifier());

            // 2️⃣ Validate password
            if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                throw new InvalidPasswordException("Invalid password");
            }

            // 3️⃣ Get Employee & Company info (if not admin)
            Long employeeId = null;
            Long companyId = null;

            String userType = user.getUserType();
            if ("SuperAdmin".equalsIgnoreCase(userType) || "Admin".equalsIgnoreCase(userType)) {
                // Admin users don't need employee/company validation
            } else {
                Integer employeeSerialId = user.getEmployeeSerialId();
                if (employeeSerialId != null) {
                    Optional<Employee> employeeOpt = employeeRepository.findByEmployeeSerialId(employeeSerialId);
                    if (employeeOpt.isPresent()) {
                        Employee employee = employeeOpt.get();
                        employeeId = employee.getId();
                        // You can extract company info here if needed:
                        companyId = employee.getCompany().getId();
                    }
                    else
                        throw new EmployeeNotFound("Employee serial id not found for the employees: " + employeeSerialId);
                }
            }

            // 4️⃣ Fetch all permissions from user's roles
            List<Permission> permissions = new ArrayList<>();
            Role role = user.getRoles();
            if (role != null && role.getPermissions() != null) {
                permissions.addAll(role.getPermissions());
            }

            // Remove duplicates
            List<Long> permissionIds = permissions.stream()
                    .map(Permission::getId)
                    .distinct()
                    .toList();

            // 5️⃣ Fetch only menus that match permission IDs or are public (null permission)
            List<Menu> allMenus = menuRepository.findMenusByPermissionIds(permissionIds);

            // 6️⃣ Build nested menu tree
            List<Menu> menuTree = buildMenuTree(allMenus, permissionIds, null);

            // 7️⃣ Generate JWT tokens
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

            // 8️⃣ Prepare role info (like Laravel)
            Map<String, Object> roleMap = new HashMap<>();
            if (role != null) {
                roleMap.put("id", role.getId());
                roleMap.put("name", role.getName());
                roleMap.put("level", role.getLevel());
                roleMap.put("created_at", role.getCreatedDate());
                roleMap.put("updated_at", role.getLastModifiedDate());
            }

            // 9️⃣ Prepare response
            if (user.getId() > 0) {
                response.setUser(user);
                response.setRole(roleMap);
                response.setPermissions(permissions);
                response.setMenus(menuTree);
                response.setEmployeeId(employeeId);
                response.setEmployeeSerialId(user.getEmployeeSerialId());
                response.setCompanyId(companyId);
                response.setToken(jwtToken);
                response.setRefreshToken(refreshToken);
                response.setExpiration(EXPIRATION_TIME);
                response.setMessage("Login successful");
                response.setStatusCode(HttpStatus.OK.value());
            }

        } catch (InvalidPasswordException | UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong during login", e);
        }

        return response;
    }

    @Override
    public AuthResponse refreshToken(TokenRequest request) {
        AuthResponse response = new AuthResponse();

        try{

            String userEmail = jwtService.extractUserName(request.refreshToken());

            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

            if (jwtService.isValidToken(request.refreshToken(), user)) {

                var jwt = jwtService.generateToken(user);

                revokedAllUserTokens(user);

                saveUserToken(user, jwt);

                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(request.refreshToken());
                response.setExpiration(EXPIRATION_TIME);
                response.setMessage("Successfully Refreshed Token");
            }

        }catch (Exception e){

            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

            response.setMessage(e.getMessage());
        }

        return  response;
    }

    private User resolveUserByIdentifier(String identifier) {
        Optional<User> userOptional;

        if (identifier.matches("^[0-9]+$")) {
            // Numeric identifier: could be phone or employeeSerialId
            if (identifier.length() == 11) {
                userOptional = userRepository.findByPhone(identifier);
                return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found against phone number: " + identifier));
            } else {
                Integer employeeSerialId = Integer.valueOf(identifier);
                userOptional = userRepository.findByEmployeeSerialId(employeeSerialId);
                return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found against employee serial id: " + identifier));
            }
        } else if (identifier.contains("@")) {
            // Email
            userOptional = userRepository.findByEmail(identifier);
            return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found against email: " + identifier));
        } else {
            throw new UsernameNotFoundException("Invalid login identifier: " + identifier);
        }

    }

    private void revokedAllUserTokens(User user)
    {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if ((validUserTokens.isEmpty()))
        {
            return;
        }

        validUserTokens.forEach(t -> {
            t.setExpired(1);
            t.setRevoked(1);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    public void saveUserToken(User user, String jwtToken)
    {
        Token token = new Token();

        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(0);
        token.setRevoked(0);

        tokenRepository.save(token);
    }

    private List<Menu> buildMenuTree(List<Menu> menus, List<Long> permissionIds, Long parentId) {
        List<Menu> menuTree = new ArrayList<>();

        for (Menu menu : menus) {
            if (Objects.equals(menu.getParentId(), parentId)) {

                // first children search (recursively)
                List<Menu> children = buildMenuTree(menus, permissionIds, menu.getId());

                // ✅ Check: permission match or children
                if (menu.getPermission() != null && permissionIds.contains(menu.getPermission().getId())) {
                    // Permission match - menu add করুন
                    menu.setChildren(children);
                    menuTree.add(menu);
                } else if (!children.isEmpty()) {
                    // Permission না থাকলেও children থাকলে parent menu add করুন
                    menu.setChildren(children);
                    menuTree.add(menu);
                }
            }
        }

        return menuTree;
    }
}
