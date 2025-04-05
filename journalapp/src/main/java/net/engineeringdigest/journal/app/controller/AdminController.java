package net.engineeringdigest.journal.app.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journal.app.cache.AppCache;
import net.engineeringdigest.journal.app.entity.User;
import net.engineeringdigest.journal.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    AppCache appCache;

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        if(users != null && !users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/reload-cache")
    public ResponseEntity<String> reloadCache() {
        try {
            appCache.init();
            return new ResponseEntity<>("Cache Updated Successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("error while refreshing the cache", e);
            throw new RuntimeException(e);
        }

    }
}
