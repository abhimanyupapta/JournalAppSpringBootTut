package net.engineeringdigest.journal.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
// find methods where transactional annotation is there.
//will create a transaction context around such methods (treated as one).
public class TransactionConfig {

    @Bean
    public PlatformTransactionManager getTransactionManagerInstance(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
    //PlatformTransactionManager is implemented by MongoTransactionManager
    //will need to tell this to spring by creating a bean.
    //mongoDatabaseFactory - is an interface - we'll be able to make connections to database.

}
