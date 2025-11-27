package za.co.pop.twitter.pop_twitter.service;

import za.co.pop.twitter.pop_twitter.model.PostDTO;

import java.util.List;

public interface FeedService {
    List<PostDTO> getFeed();
}
