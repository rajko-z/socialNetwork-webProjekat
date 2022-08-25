package repository;

import org.eclipse.jetty.server.handler.StatisticsHandler;

import util.Constants;

public class RepoFactory {

    public static UserRepository userRepo;
    public static ImageRepository imageRepo;
    public static PostRepository postRepo;
    public static CommentRepository commentRepo;
    public static FriendRequestRepository friendRequestRepo;
    public static DirectMessageRepository directMessageRepository;


    public static void loadData() {
        imageRepo = new ImageRepository(Constants.FILE_IMAGES);
        imageRepo.loadData();
        userRepo = new UserRepository(Constants.FILE_USERS);
        userRepo.loadData();
        commentRepo = new CommentRepository(Constants.FILE_COMMENTS, userRepo);
        commentRepo.loadData();
        postRepo = new PostRepository(Constants.FILE_POSTS, commentRepo, imageRepo);
        postRepo.loadData();
        friendRequestRepo = new FriendRequestRepository(Constants.FILE_FRIEND_REQUESTS, userRepo);
        friendRequestRepo.loadData();
        userRepo.setMissingBidirectionalParams(postRepo);
        directMessageRepository = new DirectMessageRepository(Constants.FILE_DIRECT_MESSAGES, userRepo);
        directMessageRepository.loadData();
        
    }

}
