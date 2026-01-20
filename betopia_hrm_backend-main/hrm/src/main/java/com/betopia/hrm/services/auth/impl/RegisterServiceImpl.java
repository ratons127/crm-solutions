package com.betopia.hrm.services.auth.impl;

import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.workplace.WorkplaceNotFound;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.exception.role.RoleNotFoundException;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.RoleRepository;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.domain.users.request.UserRequest;
import com.betopia.hrm.services.auth.RegisterService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public RegisterServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(UserRequest request) {
        User user = new User();

        user.setEmployeeSerialId(request.employeeSerialId());
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmailVerifiedAt(request.emailVerifiedAt());
        user.setRememberToken(request.rememberToken());
        user.setActive(request.isActive());

        user.setRoles(roleRepository.findById(request.roleId()).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + request.roleId())));

        return userRepository.save(user);

    }
}
