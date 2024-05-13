package com.placeholders.mindquest.journals;

import com.placeholders.mindquest.login_utils.AuthController;
import com.placeholders.mindquest.security.SqlService;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserRepository;
import jakarta.validation.Valid;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Controller
public class JournalController {
    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SqlService sqlService;

    @GetMapping("/journal")
    public String showJournalPage(Model model, @RequestParam(defaultValue = "0") int page) {
        if (!AuthController.isLoggedIn()) {
            return "redirect:/login?please";
        }

        JournalDTO journal = new JournalDTO();
        model.addAttribute("journal", journal);

        String email = AuthController.currentUser().get().getEmail();

        int pageSize = 5;
        int startIndex = page * pageSize;
        int endIndex = startIndex + pageSize;

        List<Journal> journalList = userRepository.findByEmail(email).getDiaryEntries();
        List<Journal> displayedEntries = journalList.subList(
                Math.min(startIndex, journalList.size()),
                Math.min(endIndex, journalList.size())
        );

        model.addAttribute("journalList", displayedEntries);
        model.addAttribute("currentPage", page);


        int totalEntries = journalList.size();
        int totalPages = (int) Math.ceil((double) totalEntries / pageSize);
        if (totalPages < 1) {
            totalPages = 1;
        }
        model.addAttribute("totalPages", totalPages);

        return "journal";
    }

    @PostMapping("/deleteJournal")
    public String deleteJournal(@RequestParam("id") int journalId) {

        sqlService.deleteJournal(journalId);
        return "redirect:/journal";
    }

    @PostMapping("/createJournal")
    public String createJournalEntry(@Valid @ModelAttribute("journal") Journal journal) {
        Journal newJournal = new Journal();
        newJournal.setTitle(journal.getTitle());
        newJournal.setDiaryEntry("What's on your mind...");

        Optional<User> curr = AuthController.currentUser();
        curr.ifPresent( u -> {
            val user = userRepository.findByEmail(u.getEmail());
            user.addJournal(newJournal);
            newJournal.setUser(user);
            journalRepository.save(newJournal);
        });

        return "redirect:/journal";
    }

    @PostMapping("/updateJournal")
    public String updateJournal(@RequestParam("id") int journalId, @RequestParam("content") String content) {
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

        return "redirect:/journal";
    }

    public void setJournalRepository(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }
}