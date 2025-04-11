package net.engineeringdigest.journal.app.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journal.app.entity.User;
import net.engineeringdigest.journal.app.service.LoginRegisterService;
import net.engineeringdigest.journal.app.service.UserDetailServiceImpl;
import net.engineeringdigest.journal.app.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@Slf4j
public class LoginRegisterController {

    @Autowired
    private LoginRegisterService loginRegisterService;


    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try{
            String jwt = loginRegisterService.registerUser(user);
            return new ResponseEntity<>(jwt, HttpStatus.OK );
        } catch(Exception e) {
            log.error("Error occurred for {} :",user.getUserName(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String jwt = loginRegisterService.loginUser(user);

        if(jwt != null) {
            return new ResponseEntity<>(jwt, HttpStatus.OK );
        }

        return new ResponseEntity<>("Wrong username or password", HttpStatus.BAD_REQUEST);
    }
}
