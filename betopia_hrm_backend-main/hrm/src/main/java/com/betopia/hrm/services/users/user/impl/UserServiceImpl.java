package com.betopia.hrm.services.users.user.impl;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.specification.EmployeeSpecification;
import com.betopia.hrm.domain.users.entity.PasswordPolicy;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.workplace.WorkplaceNotFound;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.exception.role.RoleNotFoundException;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.PasswordPolicyRepository;
import com.betopia.hrm.domain.users.repository.RoleRepository;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.domain.users.request.UserRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.users.specification.UserSpecification;
import com.betopia.hrm.services.users.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final CompanyRepository companyRepository;

    private final WorkplaceRepository branchRepository;

    private PasswordEncoder passwordEncoder;

    private PasswordPolicyRepository passwordPolicyRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository, CompanyRepository companyRepository, WorkplaceRepository branchRepository, PasswordPolicyRepository passwordPolicyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.branchRepository = branchRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordPolicyRepository = passwordPolicyRepository;
    }

    @Override
    public PaginationResponse<User> index(Sort.Direction direction, int page, int perPage,  String keyword) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Specification<User> spec =
                UserSpecification.globalSearch(keyword);

        Page<User> userPage = userRepository.findAll(spec, pageable);
        List<User> users = userPage.getContent();

        PaginationResponse<User> response = new PaginationResponse<>();

        response.setData(users);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All users fetch successful");

        Links links = Links.fromPage(userPage, "/user");
        response.setLinks(links);

        Meta meta = Meta.fromPage(userPage, "/user");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public User store(UserRequest request) {

        User user = new User();

        user.setEmployeeSerialId(request.employeeSerialId());
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmailVerifiedAt(request.emailVerifiedAt());
        user.setRememberToken(request.rememberToken());
        user.setActive(request.isActive());
        user.setUserType(request.userType());

        user.setRoles(roleRepository.findById(request.roleId()).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + request.roleId())));

        return userRepository.save(user);
    }

    @Override
    public User show(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return user;
    }

    @Override
    public User update(Long userId, UserRequest request) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setEmployeeSerialId(request.employeeSerialId() != null ? request.employeeSerialId() : user.getEmployeeSerialId());
        user.setName(request.name() != null ? request.name() : user.getName());
        user.setEmail(request.email() != null ? request.email() : user.getEmail());
        user.setPhone(request.phone() != null ? request.phone() : user.getPhone());
        user.setPassword(request.password() != null ? passwordEncoder.encode(request.password()) : user.getPassword());
        user.setEmailVerifiedAt(request.emailVerifiedAt() != null ? request.emailVerifiedAt() : user.getEmailVerifiedAt());
        user.setRememberToken(request.rememberToken() != null ? request.rememberToken() : user.getRememberToken());
        user.setActive(request.isActive() ? request.isActive() : user.isActive());
        user.setRoles(request.roleId() != null ? roleRepository.findById(request.roleId()).orElse(null) : user.getRoles());
        user.setUserType(request.userType() != null ? request.userType() : user.getUserType());

        return userRepository.save(user);
    }

    @Override
    public void destroy(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getId() != null) {
            // Delete all roles associated with the user
            userRepository.deleteUserRoles(userId);
        }

        userRepository.delete(user);
    }

    @Override
    public GlobalResponse passwordValidation(String password) {
        PasswordPolicy passwordPolicy=getPasswordPolicy();
        GlobalResponse response=null;
        if(password.length()<Integer.parseInt(passwordPolicy.getMinLength())) {
            response=GlobalResponse.error(null,"Password minimum length must be greater than or equal to="+passwordPolicy.getMinLength(),0);
        }
        else if(password.length()>Integer.parseInt(passwordPolicy.getMinLength())) {
            response=GlobalResponse.error(null,"Password maximum length must be less than or equal to="+passwordPolicy.getMaxLength(),0);
        }
        else{
            response = GlobalResponse.success(null, "success", HttpStatus.NO_CONTENT.value());
        }
        return response;
    }

    private PasswordPolicy getPasswordPolicy() {
        List<PasswordPolicy> passwordPolicies = passwordPolicyRepository.findAll();
        return passwordPolicies.get(0);
    }

}
