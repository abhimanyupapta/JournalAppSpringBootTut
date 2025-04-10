package net.engineeringdigest.journal.app.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journal.app.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SentimentConsumerService {

    @Autowired
    private EmailService emailService;

    //keeps listening
    @KafkaListener(topics = "weekly_sentiments", groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentimentData) {
          log.info("sending email");
          sendEmail(sentimentData);
          log.info("email sent");
    }

    private void sendEmail(SentimentData sentimentData) {
        try {
            emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
        } catch (Exception e) {
            log.error("error while sending email", e);
        }
    }
}
