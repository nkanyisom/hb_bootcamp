package za.co.pop.twitter.pop_twitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.pop.twitter.pop_twitter.model.User;
import za.co.pop.twitter.pop_twitter.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody String username) {
        return userService.registerUser(username);
    }
}
