package za.co.pop.twitter.pop_twitter.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.pop.twitter.pop_twitter.model.User;
import za.co.pop.twitter.pop_twitter.repository.UserRepository;
import za.co.pop.twitter.pop_twitter.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(String username) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }
        User newUser = new User(username, null);
        userRepository.save(newUser);
        return userRepository.findByUsername(username);
    }
}
