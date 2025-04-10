package net.engineeringdigest.journal.app.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SentimentData {
    private String sentiment;
    private String email;
}
