package net.engineeringdigest.journal.app.repository;

import net.engineeringdigest.journal.app.entity.ConfigJournalAppEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalRepo extends MongoRepository<ConfigJournalAppEntity, ObjectId> {
}
