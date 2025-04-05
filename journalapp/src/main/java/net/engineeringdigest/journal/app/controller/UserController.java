package net.engineeringdigest.journal.app.controller;

import net.engineeringdigest.journal.app.weather.api.response.WeatherResponse;
import net.engineeringdigest.journal.app.entity.User;
import net.engineeringdigest.journal.app.service.UserService;
import net.engineeringdigest.journal.app.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    WeatherService weatherService;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {

        //getting authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

         boolean updated = userService.updateUser(userName, user);
         if(updated) {
             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
         }
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById() {
        //getting authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteUser(authentication.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Shimla");
        String greeting = "";

        if(weatherResponse != null) {
            greeting = "Hi " + authentication.getName() + ", the weather feels like " + weatherService.getWeather("Shimla").getCurrent().getFeelsLikeC() + " degrees";
        } else {
            greeting = "Hi " + authentication.getName();
        }
        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }


}
