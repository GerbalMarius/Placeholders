package com.placeholders.mindquest.user_utils;


import com.placeholders.mindquest.role_utils.Role;
import com.placeholders.mindquest.role_utils.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository users, RoleRepository roles,  PasswordEncoder passwordEncoder){
        this.userRepository = users;
        this.roleRepository = roles;
        this.passwordEncoder = passwordEncoder;

    }

    public void saveUser(UserDTO userDTO){
        User user = new User(userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFirstName(), userDTO.getLastName());

        Role role = roleRepository.findByName("ROLE_ADMIN");

        if (role == null) {
            role = checkRole();
        }
        user.setRoles(List.of(role));
        userRepository.save(user);
    }

    private Role checkRole() {
        var role = new Role();
        role.setName("ROLE_ADMIN");

        return roleRepository.save(role);
    }

    public User findUserByEmail(String email){
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public List<UserDTO> findAll(){
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::convertUserToFormEntry)
                .collect(Collectors.toList());
    }

    private UserDTO convertUserToFormEntry(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setEmail(user.getEmail());

        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(userDTO.getLastName());

        return userDTO;
    }
}
