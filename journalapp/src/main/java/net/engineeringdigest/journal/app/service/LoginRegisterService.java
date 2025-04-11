package net.engineeringdigest.journal.app.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journal.app.entity.User;
import net.engineeringdigest.journal.app.repository.UserRepository;
import net.engineeringdigest.journal.app.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginRegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(User user) {
        if(userRepository.findByUserName(user.getUserName()) != null) {
            throw new RuntimeException("Duplicate User Name");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return loginUser(user);
    }

    public String loginUser(User user) {
        try{
            //checks username and password and authenticates if correct otherwise error
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            user.getUserName(),
                            user.getPassword()));

            //getting user details
            UserDetails userDetails = userDetailService.loadUserByUsername(user.getUserName());

            //generating token
            return jwtUtil.generateToken(userDetails.getUsername());
        } catch(Exception e) {
            log.error("Error occurred for {} :",user.getUserName(), e);
            return null;
        }
    }

}
