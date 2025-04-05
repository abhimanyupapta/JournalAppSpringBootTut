package net.engineeringdigest.journal.app.repository;

import net.engineeringdigest.journal.app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {

    @Autowired
    MongoTemplate mongoTemplate; //to interact with the mongoDB database

    public List<User> findUsersWithSE() {
        Query query = new Query();
        query.addCriteria(Criteria
                .where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                .and("sentimentAnalysis").is(true));
        return mongoTemplate.find(query, User.class);
    }

}
