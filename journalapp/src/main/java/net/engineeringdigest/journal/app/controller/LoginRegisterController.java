package net.engineeringdigest.journal.app.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journal.app.entity.User;
import net.engineeringdigest.journal.app.service.LoginRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<User> register(@RequestBody User user) {
        try{
            loginRegisterService.registerUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK );
        } catch(Exception e) {
            log.error("Error occurred for {} :",user.getUserName(), e);
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
    }
}
