package util;

public class Constants {
    public static final String FILE_USERS = "./static/data/users.txt";
    public static final String FILE_POSTS = "./static/data/posts.txt";
    public static final String FILE_COMMENTS = "./static/data/comments.txt";
    public static final String FILE_FRIEND_REQUESTS = "./static/data/friendRequests.txt";
    public static final String FILE_IMAGES = "./static/data/images.txt";
    public static final String FILE_DIRECT_MESSAGES = ".static/data/directMessages.txt";

    public static final String FILE_USERS_HEADER = "# id | userName | password | name | surname | dateOfBirth | gender | accountPrivate | isBlocked | role | postId for profile image | posts ids | friends ids";
    public static final String FILE_POSTS_HEADER = "# id | type of post(IMAGE, REGULAR, PROFILE) | text | list of comments ids | createdAt | imageId | isDeleted";
    public static final String FILE_COMMENTS_HEADER = "# id | user_id | text | createAt | modifiedAt | isDeleted";
    public static final String FILE_FRIEND_REQUESTS_HEADER = "# id | from user id | to user id | createdAt";
    public static final String FILE_IMAGES_HEADER = "# id | name";
    public static final String FILE_DIRECT_MESSAGE_HEADER = "# id | from user id | to user id | text | createdAt | adminSent";
}
