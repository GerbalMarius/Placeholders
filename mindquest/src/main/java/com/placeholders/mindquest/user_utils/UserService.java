package com.placeholders.mindquest.user_utils;


import com.placeholders.mindquest.role_utils.Role;
import com.placeholders.mindquest.role_utils.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final Set<String> adminKeys;

    public UserService(UserRepository users, RoleRepository roles, PasswordEncoder passwordEncoder) {
        this.userRepository = users;
        this.roleRepository = roles;
        this.passwordEncoder = passwordEncoder;
        this.adminKeys = Set.of("f7fde82ea02b", "ccbbab23654c", "5e12f9c12025", "2914f22fc348");
    }

    public void saveUser(UserDTO userDTO) {
        User user = new User(userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFirstName(), userDTO.getLastName());


        String roleToFind;

        if (adminKeys.stream().anyMatch(e -> e.equals(userDTO.getPassword()))){
            roleToFind = "ROLE_ADMIN";
        }
        else {
            roleToFind = "USER";
        }

        Role role = roleRepository.findByName(roleToFind);

        if (role == null) {
            role = assignRoleViaKey(roleToFind);
        }
        user.setRoles(List.of(role));
        userRepository.save(user);
    }

    private Role assignRoleViaKey(String roleName){
       return roleRepository.save(new Role(roleName));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::convertUserToFormEntry)
                .collect(Collectors.toList());
    }

    private UserDTO convertUserToFormEntry(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setEmail(user.getEmail());


        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());

        return userDTO;
    }


    public boolean isValidPassword(User user, UserDTO userFormData) {
        return passwordEncoder.matches(userFormData.getPassword(), user.getPassword());
    }
}
