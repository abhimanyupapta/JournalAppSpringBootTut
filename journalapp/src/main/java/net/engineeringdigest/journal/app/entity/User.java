package net.engineeringdigest.journal.app.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

     @Id
     private ObjectId id;
     @Indexed(unique = true)
     @NonNull
     private String userName;
     @NonNull
     private String password;
     private String email;
     private boolean sentimentAnalysis;
     @DBRef //this list would contain the reference to journal entries collection.
     private List<JournalEntry> journalEntries = new ArrayList<>();
     private List<String> roles;

}
