package com.placeholders.mindquest.journals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JournalController.class)
public class JournalControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JournalRepository journalRepository;
    private JournalController journalController;
    private JournalService journalService;
    private Model model;


    @BeforeEach
    public void setUp() {
        journalRepository = mock(JournalRepository.class);
        model = mock(Model.class);
        journalController = new JournalController(journalService);
        journalController.setJournalRepository(journalRepository);
    }

    @Test
    public void testShowJournalPage() throws Exception {
        List<Journal> journals = new ArrayList<>();
        when(journalRepository.findAll()).thenReturn(journals);
        String viewName = journalController.showJournalPage(model,0);
        verify(model).addAttribute(eq("journal"), any(JournalDTO.class));
        verify(model).addAttribute("journalList", journals);
        assertEquals("journals", viewName);
    }

    @Test
    public void testDeleteJournal() {
        int journalId = 1;

        journalController.deleteJournal(journalId);

        verify(journalRepository).deleteById(journalId);
    }

    @Test
    public void testCreateJournalEntry() {
        JournalRepository journalRepository = mock(JournalRepository.class);

        JournalController journalController = new JournalController(journalService);

        journalController.setJournalRepository(journalRepository);

        Journal journalArgument = new Journal();

        journalArgument.setTitle("Test Title");

        when(journalRepository.save(any(Journal.class))).thenAnswer(invocation -> {
            Journal savedJournal = invocation.getArgument(0);

            assertEquals(journalArgument.getTitle(), savedJournal.getTitle());

            assertEquals("Insert text", savedJournal.getDiaryEntry());

            return savedJournal;
        });
        journalController.createJournalEntry(journalArgument);
        verify(journalRepository).save(any(Journal.class));
    }
    @Test
    public void testJournalUpdateWithInvalidID() throws Exception {
        int invalidJournalId = 123;
        mockMvc.perform(post("/updateJournal")
                        .param("id", String.valueOf(invalidJournalId))
                        .param("content", "Updated content"))
                .andExpect(status().isForbidden());

        verify(journalRepository, never()).save(any(Journal.class));
    }
    @Test
    public void testUpdateJournal() {
        int journalId = 1;
        String content = "Updated content";
        Journal journal = new Journal();
        journal.setDiaryEntry("Original content");

        when(journalRepository.findById(anyInt())).thenReturn(Optional.of(journal));

        journalController.updateJournal(journalId, content);

        assert(journal.getDiaryEntry().equals(content));
        verify(journalRepository).save(journal);
    }

}