package za.co.pop.twitter.pop_twitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.pop.twitter.pop_twitter.model.PostDTO;
import za.co.pop.twitter.pop_twitter.service.FeedService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping
    public List<PostDTO> getFeed() {
        return feedService.getFeed();
    }
}
