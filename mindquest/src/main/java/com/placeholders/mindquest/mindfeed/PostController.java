package com.placeholders.mindquest.mindfeed;

import com.placeholders.mindquest.journals.Journal;
import com.placeholders.mindquest.journals.JournalDTO;
import com.placeholders.mindquest.login_utils.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/mindfeed")
    public String getAllPosts(Model model, @RequestParam(defaultValue = "0") int page) {
        if (!AuthController.isLoggedIn()){
            return "redirect:/login?please";
        }

        int pageSize = 5;
        int startIndex = page * pageSize;
        int endIndex = startIndex + pageSize;

        List<Post> posts = postService.getPostsPagedLatest(page, pageSize);

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);


        long totalPages = postService.getTotalPageCount(5);
        if (totalPages < 1) {
            totalPages = 1;
        }
        model.addAttribute("totalPages", totalPages);

        return "mindfeed";
    }
}