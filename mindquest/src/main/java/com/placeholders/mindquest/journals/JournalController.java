package com.placeholders.mindquest.journals;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class JournalController {
    @Autowired
    private JournalRepository journalRepository;

    @GetMapping("/journal")
    public String showRegisterPage(Model model){
        JournalDTO journal = new JournalDTO();
        model.addAttribute("journal", journal);
        List<Journal> journalList = journalRepository.findAll();
        model.addAttribute("journalList", journalList);
        return "journal";
    }
  @PostMapping("/deleteJournal")
  public String deleteJournal(@RequestParam("id") int journalId) {

      journalRepository.deleteById(journalId);
      return "redirect:/journal";
  }

  @PostMapping("/createJournal")
  public String createJournalEntry(@Valid @ModelAttribute("journal") Journal journal) {
        Journal newJournal = new Journal();
        newJournal.setTitle(journal.getTitle());
        newJournal.setDiaryEntry("What's on your mind...");

        journalRepository.save(newJournal);
        return "redirect:/journal";
  }
 @PostMapping("/updateJournal")
 public String updateJournal(@RequestParam("id") int journalId, @RequestParam("content") String content) {
     Journal journal = journalRepository.findById(journalId).orElse(null);
     if (journal != null) {
         journal.setDiaryEntry(content);
         journalRepository.save(journal);
     }
     return "redirect:/journal";
 }
    public void setJournalRepository(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }
}

