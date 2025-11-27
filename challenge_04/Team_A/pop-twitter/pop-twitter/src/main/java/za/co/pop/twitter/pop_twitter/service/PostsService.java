package za.co.pop.twitter.pop_twitter.service;

import za.co.pop.twitter.pop_twitter.model.Post;

import java.util.List;

public interface PostsService {
    Post createPost(Long userId, String post);
    List<Post> getPostsByUserId(Long userId);
}
