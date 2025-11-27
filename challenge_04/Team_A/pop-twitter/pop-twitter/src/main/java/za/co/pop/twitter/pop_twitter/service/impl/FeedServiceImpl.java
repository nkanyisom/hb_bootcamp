package za.co.pop.twitter.pop_twitter.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import za.co.pop.twitter.pop_twitter.model.Post;
import za.co.pop.twitter.pop_twitter.model.PostDTO;
import za.co.pop.twitter.pop_twitter.model.User;
import za.co.pop.twitter.pop_twitter.repository.PostRepository;
import za.co.pop.twitter.pop_twitter.repository.UserRepository;
import za.co.pop.twitter.pop_twitter.service.FeedService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<PostDTO> getFeed() {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return posts.stream().map(post -> {
            User user = userRepository.findById(post.getUserId()).orElse(null);
            String username = (user != null) ? user.getUsername() : "Unknown";
            return new PostDTO(post.getId(), post.getUserId(), post.getPost(), username);
        }).collect(Collectors.toList());
    }
}
