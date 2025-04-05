package net.engineeringdigest.journal.app.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config_journal")
@Data
@Builder
public class ConfigJournalAppEntity {
    private String key;
    private String value;
}
