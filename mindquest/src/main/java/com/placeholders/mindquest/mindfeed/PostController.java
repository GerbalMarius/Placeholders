package com.placeholders.mindquest.mindfeed;

import com.placeholders.mindquest.login_utils.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/mindfeed")
    public String getAllPosts(Model model) {
        if (!AuthController.isLoggedIn()){
            return "redirect:/login?please";
        }

        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);

        return "mindfeed";
    }
}