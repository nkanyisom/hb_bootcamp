package za.co.pop.twitter.pop_twitter.model;

public class PostDTO {

    private Long id;
    private Long userId;
    private String post;
    private String username;

    public PostDTO(Long id, Long userId, String post, String username) {
        this.id = id;
        this.userId = userId;
        this.post = post;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
