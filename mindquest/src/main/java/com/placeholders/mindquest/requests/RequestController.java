package com.placeholders.mindquest.requests;


import com.placeholders.mindquest.login_utils.AuthController;
import com.placeholders.mindquest.security.SqlService;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RequestController {

    private final RequestRepository requestRepository;

    private final SqlService sqlService;

    private final UserRepository userRepository;


    public RequestController(RequestRepository requestRepository, SqlService sqlService, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.sqlService = sqlService;
        this.userRepository = userRepository;
    }

    @GetMapping("/users/requests")
    public String loadUserRequests(Model model) {
        List<Request> requests = requestRepository.findAll();
        model.addAttribute("requests", requests);
        return "requests";
    }

    @GetMapping("/request")
    public String loadDashboardRequest(Model model) {
        var dto = new RequestDTO();
        model.addAttribute("request", dto);
        return "request-dashboard";
    }

    @PostMapping("/dashboard/request/create")
    public String createRequest(@ModelAttribute("request") Request request) {

        if (!AuthController.isLoggedIn()){
            return "redirect:/login?please";
        }

        var newRequest = new Request(request.getMessage());

        final User actualUser = userRepository.findByEmail(AuthController.currentUser().get().getEmail());

        newRequest.setUser(actualUser);
        newRequest.setEmail(actualUser.getEmail());

        requestRepository.save(newRequest);
        return "redirect:/request?success";
    }
}