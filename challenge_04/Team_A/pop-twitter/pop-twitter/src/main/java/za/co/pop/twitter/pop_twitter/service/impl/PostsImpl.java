package za.co.pop.twitter.pop_twitter.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.pop.twitter.pop_twitter.model.Post;
import za.co.pop.twitter.pop_twitter.repository.PostRepository;
import za.co.pop.twitter.pop_twitter.repository.UserRepository;
import za.co.pop.twitter.pop_twitter.service.PostsService;

import java.util.List;

@Service
public class PostsImpl implements PostsService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Post createPost(Long userId, String post) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Post newPost = new Post(userId, post);
        return postRepository.save(newPost);
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }
}
