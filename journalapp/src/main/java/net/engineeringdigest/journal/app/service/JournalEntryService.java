package net.engineeringdigest.journal.app.service;


import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journal.app.entity.JournalEntry;
import net.engineeringdigest.journal.app.entity.User;
import net.engineeringdigest.journal.app.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    //private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryAudioService journalEntryAudioService;

    @Autowired
    private S3Service s3Service;

    @Transactional //if anything fails all the successful ones will get rolled back
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {

            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            journalEntry.setId(new ObjectId());

            String audioKey = journalEntryAudioService.textToSpeechAndUpload(user.getId(), journalEntry.getId(), journalEntry.getContent(), s3Service);
            journalEntry.setAudioKey(audioKey);

            JournalEntry saved = journalEntryRepo.save(journalEntry);

            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            log.error("Error occurred for {} :",userName, e);
            throw new RuntimeException("An error has occurred while saving the entry", e);
        }
     }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntry.setDate(LocalDateTime.now());
        journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id, String userName) {
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntryCollection = user.getJournalEntries().stream().filter(e -> e.getId().equals(id)).collect(Collectors.toList());
        if(!journalEntryCollection.isEmpty()) {
            return journalEntryRepo.findById(journalEntryCollection.get(0).getId());
        }
        return Optional.empty();
    }

    @Transactional
    public void deleteById(ObjectId id, String userName) {
        try {
            User user = userService.findByUserName(userName);
            user.getJournalEntries().removeIf(x  -> x.getId().equals(id));
            userService.saveUser(user);
            journalEntryRepo.deleteById(id);
        } catch(Exception e) {
            log.error("Error occurred for {} :",userName, e);
            throw new RuntimeException("An error has occurred while deleting the entry", e);
        }

    }

    public JournalEntry updateById(ObjectId id, JournalEntry req, String userName) {
        JournalEntry old = findById(id, userName).orElse(null);
        if(old != null) {
            old.setTitle(req.getTitle().isEmpty() ? old.getTitle() : req.getTitle());
            old.setContent(req.getContent() != null && req.getContent().isEmpty() ? old.getContent() : req.getContent());
            saveEntry(old);
        }
        return old;
    }
}
