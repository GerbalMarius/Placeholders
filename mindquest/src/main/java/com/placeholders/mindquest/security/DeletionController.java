package com.placeholders.mindquest.security;

import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeletionController {

    @Autowired
    private UserService userService;

    @Autowired
    private SqlService sqlService;

    @GetMapping("/user/delete")
    public String delete(String email) {
        final User toBeDeleted = userService.findUserByEmail(email);

        sqlService.deleteUser(toBeDeleted.getEmail());

        return "redirect:/users";
    }
}
