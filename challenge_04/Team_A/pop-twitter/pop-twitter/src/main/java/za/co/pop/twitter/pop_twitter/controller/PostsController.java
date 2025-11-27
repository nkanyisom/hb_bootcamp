package za.co.pop.twitter.pop_twitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.pop.twitter.pop_twitter.model.Post;
import za.co.pop.twitter.pop_twitter.service.PostsService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private PostsService postsService;

    @PostMapping("/{userId}")
    public Post createPost(@PathVariable Long userId, @RequestBody String post) {
        return postsService.createPost(userId, post);
    }

    @GetMapping("/{userId}")
    public List<Post> getPostsByUserId(@PathVariable Long userId) {
        return postsService.getPostsByUserId(userId);
    }
}
