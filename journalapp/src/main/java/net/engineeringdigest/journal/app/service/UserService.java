package net.engineeringdigest.journal.app.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journal.app.entity.User;
import net.engineeringdigest.journal.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRegisterService loginRegisterService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public List<User> getAll() {
         return userRepository.findAll();
    }

    public boolean updateUser(String userName, User user) {
        if(user != null) {
            User old = userRepository.findByUserName(userName);
            if(old != null) {
                old.setUserName(user.getUserName().isEmpty() ? old.getUserName() : user.getUserName());
                old.setPassword(user.getPassword().isEmpty() ? old.getPassword() : passwordEncoder.encode(user.getPassword()));
                saveUser(old);
                return true;
            }
        }
        return false;
    }

    public void deleteUser(String userName) {
         userRepository.deleteByUserName(userName);
    }
}
