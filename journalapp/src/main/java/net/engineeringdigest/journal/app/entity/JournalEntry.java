package net.engineeringdigest.journal.app.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "journal_entries") //storing as a document in db
@Data //getter setter constructors
@NoArgsConstructor
public class JournalEntry {

    @Id //annotation to tell that it is a primary key
    private ObjectId id;

    @NonNull
    private String title;

    private String AudioKey;

    private String content;

    private LocalDateTime date;

//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public void setId(ObjectId id) {
//        this.id = id;
//    }
//
//    public void setDate(LocalDateTime date) {
//        this.date = date;
//    }
//
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public ObjectId getId() {
//        return id;
//    }
//
//    public LocalDateTime getDate() {
//        return date;
//    }
}
