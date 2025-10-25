package org.example.controller;

import org.example.entity.NewsLetterModel;
import org.example.service.EmailService;
import org.example.service.NewsLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/newsletter")
@CrossOrigin(origins = "https://aisolai.vercel.app")
public class NewsLetterController {

    @Autowired
    private NewsLetterService newsletterService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(@RequestParam Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                return ResponseEntity.badRequest().body(response);
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                response.put("success", false);
                response.put("message", "Invalid email format");
                return ResponseEntity.badRequest().body(response);
            }
            newsletterService.subscribe(email);

            response.put("success", true);
            response.put("message", "Successfully subscribed to newsletter!");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred while subscribing");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Alternative method using @RequestBody with Map
    @PostMapping("/subscribe-json")
    public ResponseEntity<Map<String, Object>> subscribeJson(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");

            if (email == null || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                return ResponseEntity.badRequest().body(response);
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                response.put("success", false);
                response.put("message", "Invalid email format");
                return ResponseEntity.badRequest().body(response);
            }

            newsletterService.subscribe(email);

            response.put("success", true);
            response.put("message", "Successfully subscribed to newsletter!");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred while subscribing");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Map<String, Object>> unsubscribe(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            newsletterService.unsubscribe(email);

            response.put("success", true);
            response.put("message", "Successfully unsubscribed from newsletter");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred while unsubscribing");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/subscribers")
    public ResponseEntity<Map<String, Object>> getAllSubscribers() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<NewsLetterModel> subscribers = newsletterService.getAllSubscribers();

            response.put("success", true);
            response.put("count", subscribers.size());
            response.put("subscribers", subscribers);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch subscribers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkSubscription(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean isSubscribed = newsletterService.isSubscribed(email);

            response.put("success", true);
            response.put("email", email);
            response.put("isSubscribed", isSubscribed);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to check subscription status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // Manual trigger for sending weekly newsletter (for testing)
    @PostMapping("/send-weekly")
    public ResponseEntity<Map<String, Object>> sendWeeklyNewsletter(@RequestParam(required = false) String content) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (content == null || content.trim().isEmpty()) {
                content = getDefaultNewsletterContent();
            }

            newsletterService.weeklyNewsletter(content);

            response.put("success", true);
            response.put("message", "Weekly newsletter sent successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to send newsletter: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String getDefaultNewsletterContent() {
        return """
            <h2>This Week in AI</h2>
            <p>Welcome to this week's edition of ReAiSol insights!</p>
            
            <h3>Featured Article</h3>
            <p>Discover how AI is transforming business operations...</p>
            
            <h3>Latest Updates</h3>
            <ul>
                <li>New AI technologies revolutionizing industries</li>
                <li>Best practices for AI implementation</li>
                <li>Success stories from our clients</li>
            </ul>
            
            <h3>Upcoming Events</h3>
            <p>Join us for our upcoming webinar on AI-driven solutions.</p>
            
            <p>Stay innovative,<br>The ReAiSol Team</p>
            """;
    }
}