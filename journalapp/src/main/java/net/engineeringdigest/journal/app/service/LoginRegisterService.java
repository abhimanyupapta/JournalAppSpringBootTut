package net.engineeringdigest.journal.app.service;

import net.engineeringdigest.journal.app.entity.User;
import net.engineeringdigest.journal.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginRegisterService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerUser(User user) {
        if(userRepository.findByUserName(user.getUserName()) != null) {
            throw new RuntimeException("Duplicate User Name");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
