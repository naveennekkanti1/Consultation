package org.example.service;

import com.mongodb.client.result.UpdateResult;
import org.example.entity.NewsLetterModel;
import org.example.repository.NewsLetterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NewsLetterService {
    @Autowired
    private NewsLetterRepository newsletterRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailServiceNewsLetter emailServiceNewsLetter;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void subscribe(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        NewsLetterModel existing = mongoTemplate.findOne(query, NewsLetterModel.class);

        if (existing != null) {
            if (existing.isSubscribed()) {
                throw new RuntimeException("Email is already subscribed to the newsletter.");
            }
            Update update = new Update().set("isSubscribed", true);
            mongoTemplate.updateFirst(query, update, NewsLetterModel.class);
            log.info("Re-subscribed successfully: {}", email);

            emailServiceNewsLetter.sendWelcomeEmail(email);
            return;
        }
        NewsLetterModel newSubscriber = new NewsLetterModel();
        newSubscriber.setEmail(email);
        newSubscriber.setSubscribed(true);
        mongoTemplate.save(newSubscriber);

        log.info("New subscription added: {}", email);
        emailServiceNewsLetter.sendWelcomeEmail(email);
    }

    public void weeklyNewsletter(String content) {
        List<NewsLetterModel> subscribedList = newsletterRepository.findByIsSubscribed(true);

        for (NewsLetterModel newsLetter : subscribedList) {
            try {
                emailServiceNewsLetter.sendWeeklyEmail(newsLetter.getEmail(), content);
            } catch (Exception e) {
                // Log the error instead of returning
                System.err.println("Failed to send email to "
                        + newsLetter.getEmail()
                        + ": " + e.getMessage());
            }
        }
    }
    public void unsubscribe(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        Update update = new Update().set("isSubscribed", false);
        UpdateResult result = mongoTemplate.updateFirst(query, update, NewsLetterModel.class);

        if (result.getMatchedCount() == 0) {
            throw new RuntimeException("This email is not subscribed to the newsletter.");
        }
        log.info("Successfully unsubscribed: {}", email);
    }

    public List<NewsLetterModel> getAllSubscribers() {
        return newsletterRepository.findAll();
    }

    public boolean isSubscribed(String email) {
        return newsletterRepository.findByEmail(email)
                .map(NewsLetterModel::isSubscribed)
                .orElse(false);
    }

}
