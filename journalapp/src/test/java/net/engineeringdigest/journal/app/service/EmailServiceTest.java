package net.engineeringdigest.journal.app.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendMail() {
        emailService.sendEmail(
                "realbadrabbit23@gmail.com",
                "Job Opportunity",
                "Hi Sir, \nPlease consider me \nRegards, \nAbhi");
    }
}
