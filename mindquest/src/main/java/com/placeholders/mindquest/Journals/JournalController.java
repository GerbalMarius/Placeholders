package com.placeholders.mindquest.Journals;

import com.fasterxml.jackson.databind.JsonNode;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserDTO;
import com.placeholders.mindquest.user_utils.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class JournalController {
    @Autowired
    private JournalRepository journalRepository;

    @GetMapping("/journals")
    public String showRegisterPage(org.springframework.ui.Model model){
        JournalDTO journal = new JournalDTO();
        model.addAttribute("journal", journal);
        List<Journal> journalList = journalRepository.findAll();
        model.addAttribute("journalList", journalList);
        return "journals";
    }
  /*  @GetMapping("/journals")
    public String viewJournals(Model model) {
        List<Journal> journalList = journalRepository.findAll(); // Retrieve all journal entries from the database
        model.addAttribute("journalList", journalList); // Add the list to the model
        return "journals"; // Return the Thymeleaf template name
    }
    */
  @PostMapping("/deleteJournal")
  public String deleteJournal(@RequestParam("id") int journalId) {

      journalRepository.deleteById(journalId);
      return "redirect:/journals";
  }
  @PostMapping("/createJournal")
  public String createJournalEntry(@Valid @ModelAttribute("journal") Journal journal) {
        Journal newJournal = new Journal();
        newJournal.setTitle(journal.getTitle());
        newJournal.setDiaryEntry("Insert text");

        journalRepository.save(newJournal);
        return "redirect:/journals";
  }
 /*   @PostMapping("/updateJournal")
    public String updateJournal(@RequestParam("id") int journalId,@ModelAttribute("diaryEntry") String content) {
        Journal journal = journalRepository.findById(journalId).orElse(null);
        if (journal != null) {
            journal.setDiaryEntry(content);
            journalRepository.save(journal);
        }
        return "redirect:/journals";
    }*/
 @PostMapping("/updateJournal")
 public String updateJournal(@RequestParam("id") int journalId, @RequestParam("content") String content) {
     Journal journal = journalRepository.findById(journalId).orElse(null);
     if (journal != null) {
         journal.setDiaryEntry(content);
         journalRepository.save(journal);
     }
     return "redirect:/journals";
 }

}
