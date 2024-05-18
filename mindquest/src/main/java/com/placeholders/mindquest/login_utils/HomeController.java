package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.mindfeed.Post;
import com.placeholders.mindquest.mindfeed.PostService;
import com.placeholders.mindquest.settings.ProfilePhoto;
import com.placeholders.mindquest.settings.ProfilePhotoRepository;
import com.placeholders.mindquest.timestamp.TimeStamp;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserDTO;
import com.placeholders.mindquest.user_utils.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProfilePhotoRepository profilePhotoRepository;

    @Autowired
    private UserService userService;

    private final PostService postService;

    @Autowired
    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public String home(){
        return "index";
    }

    @GetMapping("/mindboard")
    public String mindboard(Model model){

        if (!AuthController.isLoggedIn() && AuthController.firstTimeUser().isEmpty()){
            return "redirect:/login?please";
        }
        var user = getUser();


        model.addAttribute("currentUser", user);

        boolean isFirstTime = AuthController.firstTimeUser().isPresent();

        model.addAttribute("startingQuizNotTaken",isFirstTime);


        ProfilePhoto photo = profilePhotoRepository.findById(user.getId());
        if(photo != null)
        {
            byte[] profilePhotoData = photo.getData();
            String base64ImageData = Base64.getEncoder().encodeToString(profilePhotoData);
            model.addAttribute("photo",photo);
            model.addAttribute("profilePhotoData", base64ImageData);
        }

        final User actual = userService.findUserByEmail(user.getEmail());
        List<TimeStamp> stamps = actual.getTimestampLog();
        List<Integer> points = new ArrayList<>();
        List<LocalDateTime> localDateTimeList = new ArrayList<>();

        for(TimeStamp stamp : stamps) {
            points.add(stamp.getPoints());
            localDateTimeList.add(stamp.getTimestamp());
        }

        List<LocalDateTime> firstSeven = localDateTimeList.subList(0, Math.min(7, localDateTimeList.size()));
        Collections.reverse(firstSeven);
        List<Integer> firstSevenPoints = points.subList(0, Math.min(7, points.size()));
        Collections.reverse(firstSevenPoints);

        List<String> dates = firstSeven.stream()
                .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .toList();

        double average = Math.round(firstSevenPoints.stream().mapToInt(Integer::intValue).average().orElse(0.0));

        System.out.print(firstSevenPoints);
        System.out.print(dates);
        System.out.print(average);

        model.addAttribute("points", firstSevenPoints);
        model.addAttribute("dates", dates);
        model.addAttribute("average", average);

        List<Post> posts = postService.getPostsPagedLatest(0, 3);

        model.addAttribute("posts", posts);

        return "mindboard";
    }

    private static UserDTO getUser() {
        if (AuthController.firstTimeUser().isPresent()){
            return AuthController.firstTimeUser().get();
        }
        else if (AuthController.currentUser().isPresent()){

            return AuthController.currentUser()
                    .map(User::getTransferableData)
                    .orElse(new UserDTO());
        }
        throw new NoSuchElementException("No users currently present!");
    }
}