package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "NEWS_LETTER_SUBSCRIPTION")
public class NewsLetterModel {
    @Indexed(unique = true)
    private String email;
    private Instant created_at=Instant.now();
    private boolean isSubscribed;
    private long no_of_newsletters_sent;
}
