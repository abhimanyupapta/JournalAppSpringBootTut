package net.engineeringdigest.journal.app.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journal.app.cache.AppCache;
import net.engineeringdigest.journal.app.entity.JournalEntry;
import net.engineeringdigest.journal.app.entity.User;
import net.engineeringdigest.journal.app.enums.Sentiment;
import net.engineeringdigest.journal.app.repository.UserRepositoryImpl;
import net.engineeringdigest.journal.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;


    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 * * SUN")//cron expression can be generated from cron site
    public void fetchUsersAndSendMail() {
        List<User> users = userRepository.findUsersWithSE();


        for(User u : users) {
            List<JournalEntry> journalEntries = u.getJournalEntries();

            log.info("getting journal entries of last 7 days");
            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(j -> j
                            .getDate()
                            .isAfter(LocalDateTime.now().minusDays(7)))
                    .map(j -> j.getSentiment())
                    .collect(Collectors.toList());

            log.info("getting most frequent sentiment");
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();

            for(Sentiment sentiment : sentiments) {
                if(sentiment != null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }

            Sentiment mostFreqSentiment = null;

            int maxCount = 0;

            for(Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if(entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFreqSentiment = entry.getKey();
                }
            }

            log.info("most freq sentiment is: {}", mostFreqSentiment);
            if(mostFreqSentiment != null) {
                log.info("sending email to the user");
                emailService.sendEmail(u.getEmail(), "Sentiment for last 7 days", mostFreqSentiment.toString());
            }
        }


    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearCache() {
        appCache.init();
    }
}
