package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.mindfeed.Post;
import com.placeholders.mindquest.mindfeed.PostService;
import com.placeholders.mindquest.settings.ProfilePhoto;
import com.placeholders.mindquest.settings.ProfilePhotoRepository;
import com.placeholders.mindquest.starting_quiz.StartingQuizRepository;
import com.placeholders.mindquest.timestamp.TimeStamp;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserDTO;
import com.placeholders.mindquest.user_utils.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequestMapping("/")
public class HomeController {

    private final ProfilePhotoRepository profilePhotoRepository;

    private final UserService userService;

    private final PostService postService;

    private final StartingQuizRepository startingQuizRepository;

    public HomeController(PostService postService, ProfilePhotoRepository profilePhotoRepository, UserService userService
    , StartingQuizRepository startingQuizRepository) {
        this.postService = postService;
        this.profilePhotoRepository = profilePhotoRepository;
        this.userService = userService;
        this.startingQuizRepository = startingQuizRepository;
    }

    @GetMapping("")
    public String home(){
        return "index";
    }

    @GetMapping("/mindboard")
    public String mindboard(Model model){

        if (!AuthController.isLoggedIn()){
            return "redirect:/login?please";
        }
        var user = getUser();
        model.addAttribute("currentUser", user);



        ProfilePhoto photo = profilePhotoRepository.findById(user.getId());
        if(photo != null)
        {
            byte[] profilePhotoData = photo.getData();
            String base64ImageData = Base64.getEncoder().encodeToString(profilePhotoData);
            model.addAttribute("photo",photo);
            model.addAttribute("profilePhotoData", base64ImageData);
        }

        final User actual = userService.findUserByEmail(user.getEmail());

        boolean isFirstTime = startingQuizRepository.findByUserId(actual.getId()) == null;

        model.addAttribute("startingQuizNotTaken",isFirstTime);
        processTimestamps(actual, model);

        timeLeftTillDailyQuiz(model);

        List<Post> posts = postService.getPostsPagedLatest(0, 3);

        model.addAttribute("posts", posts);

        return "mindboard";
    }

    private void processTimestamps(User actual, Model model) {

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

        model.addAttribute("points", firstSevenPoints);
        model.addAttribute("dates", dates);
        model.addAttribute("average", average);
    }

    private void timeLeftTillDailyQuiz(Model model) {

        LocalTime now = LocalTime.now();
        LocalTime midnight = LocalTime.MAX; //23:59:59.59

        long hoursLeft = now.until(midnight, ChronoUnit.HOURS);
        long minutesLeft = now.until(midnight, ChronoUnit.MINUTES) % 60;

        String timeLeft = String.format("%d hours %d minutes", hoursLeft, minutesLeft);

        model.addAttribute("timeLeft", timeLeft);
    }

    private static UserDTO getUser() {
        if (AuthController.currentUser().isPresent()){

            return AuthController.currentUser()
                    .map(User::getTransferableData)
                    .orElse(new UserDTO());
        }
        throw new NoSuchElementException("No users currently present!");
    }
}