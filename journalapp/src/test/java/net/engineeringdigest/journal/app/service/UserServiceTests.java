package net.engineeringdigest.journal.app.service;

import net.engineeringdigest.journal.app.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@SpringBootTest //annotation to start the application
public class UserServiceTests {

    @Autowired
    UserRepository userRepository;

    //@BeforeAll - run before all tests
    //@BeforeEach - run beofre each test
    //@AfterAll run after tests
    //use case? open and close a resource

    @Test
    public void testFindByUserName() {
        //different assert methods are provided to test diff senarios
        assertNotNull(userRepository.findByUserName("userTest1"));
    }

    @ParameterizedTest
    //we can also take input from csv file
    @CsvSource({
            "ADMIN",
            "userTest1"
    })
    public void testFindByUserName(String userName) {
        assertNotNull(userRepository.findByUserName("userTest1"));
    }


}
