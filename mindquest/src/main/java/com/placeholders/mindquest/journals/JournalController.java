package com.placeholders.mindquest.journals;

import com.placeholders.mindquest.login_utils.AuthController;
import com.placeholders.mindquest.mindfeed.PostService;
import com.placeholders.mindquest.security.SqlService;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserRepository;
import com.placeholders.mindquest.user_utils.UserService;
import jakarta.validation.Valid;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class JournalController {
    @Autowired
    private UserService userService;

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SqlService sqlService;

    private final JournalService journalService;

    @Autowired
    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    @GetMapping("/journal")
    public String showJournalPage(Model model, @RequestParam(defaultValue = "0") int page) {
        if (!AuthController.isLoggedIn()) {
            return "redirect:/login?please";
        }
        JournalDTO journal = new JournalDTO();
        model.addAttribute("journal", journal);

        Optional<User> currUser = AuthController.currentUser();

        currUser.ifPresent(user -> {
            final User actual = userService.findUserByEmail(user.getEmail());
            model.addAttribute("user", actual);

            int pageSize = 5;

            List<Journal> journalList = journalService.getJournalPagedLatest(page, pageSize, actual);

            model.addAttribute("journalList", journalList);
            model.addAttribute("currentPage", page);


            long totalPages = journalService.getTotalPageCount(pageSize, actual);
            if (totalPages < 1) {
                totalPages = 1;
            }
            model.addAttribute("totalPages", totalPages);
        });

        return "journal";
    }

    @PostMapping("/deleteJournal")
    public String deleteJournal(@RequestParam("id") int journalId,@RequestParam("page") int page) {
        long count = journalRepository.count();
        if(count % 5 == 1 && page != 0 && page != 1) page--;
        sqlService.deleteJournal(journalId);
        return "redirect:/journal?page="+page;
    }

    @PostMapping("/createJournal")
    public String createJournalEntry(@Valid @ModelAttribute("journal") Journal journal, @RequestParam("page") int page) {
        Journal newJournal = new Journal();
        newJournal.setTitle(journal.getTitle());
        newJournal.setDiaryEntry("What's on your mind...");

        Optional<User> curr = AuthController.currentUser();
        curr.ifPresent( u -> {
            val user = userRepository.findByEmail(u.getEmail());
            newJournal.setDate(LocalDateTime.now());
            user.addJournal(newJournal);
            newJournal.setUser(user);
            journalRepository.save(newJournal);
        });

        return "redirect:/journal?page="+page;
    }

    @PostMapping("/updateJournal")
    public String updateJournal(@RequestParam("id") int journalId, @RequestParam("content") String content,@RequestParam("page") int page) {
        val foundJournal = journalRepository.findById(journalId);
//     if (journal != null) {
//         journal.setDiaryEntry(content);
//         journalRepository.save(journal);
//     }
        if (foundJournal.isPresent()) {
            val journal = foundJournal.get();
            journal.setDiaryEntry(content);
            journalRepository.save(journal);
        }

        return "redirect:/journal?page="+page;
    }

    public void setJournalRepository(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }
}