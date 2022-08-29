package services;

import dto.comment.NewCommentDTO;
import dto.comment.UpdatedCommentDTO;
import exceptions.BadRequestException;
import exceptions.ForbiddenAccessException;
import model.Comment;
import model.FriendStatus;
import model.Post;
import model.User;
import repository.CommentRepository;
import repository.RepoFactory;
import util.Constants;
import validation.ValidationService;

import java.time.LocalDateTime;

public class CommentService {

    private final PostService postService;
    private final FriendStatusService friendStatusService;
    private final CommentRepository commentRepository;
    private final ValidationService validationService;

    public CommentService() {
        this.postService = new PostService(RepoFactory.postRepo);
        this.friendStatusService = new FriendStatusService();
        this.commentRepository = RepoFactory.commentRepo;
        this.validationService = new ValidationService();
    }

    public Comment create(NewCommentDTO newCommentDTO, User user) {
        String errorMsg = validationService.validateAndGetErrorMessage(newCommentDTO);
        if (errorMsg != null) {
            throw new BadRequestException(errorMsg);
        }
        Post postForComment = postService.getById(newCommentDTO.getPostId());
        if (postForComment == null)
            throw new BadRequestException("Not found. Can't find post with id: " + newCommentDTO.getPostId());

        User ownerOfPost = postService.getUserFromPost(postForComment.getId());
        if (ownerOfPost.isAccountPrivate()) {
            FriendStatus friendStatus = friendStatusService.getFriendStatusBetweenUsers(user, ownerOfPost);
            if (!friendStatus.equals(FriendStatus.FRIENDS))
                throw new ForbiddenAccessException("Can't comment on post from private profile. First become friend with this user");
        }

        Comment comment = new Comment(newCommentDTO.getText(), user);
        comment = commentRepository.add(comment);

        postForComment.addComment(comment);

        commentRepository.saveData(Constants.FILE_COMMENTS_HEADER);
        RepoFactory.postRepo.saveData(Constants.FILE_POSTS_HEADER);

        return comment;
    }

    public Comment update(UpdatedCommentDTO updatedCommentDTO, User user) {
        String errorMsg = validationService.validateAndGetErrorMessage(updatedCommentDTO);
        if (errorMsg != null) {
            throw new BadRequestException(errorMsg);
        }

        Comment commentToUpdate = commentRepository.getById(updatedCommentDTO.getCommentId());
        if (commentToUpdate == null) {
            throw new BadRequestException("Can't find comment with id: " + updatedCommentDTO.getCommentId());
        }

        if (!commentToUpdate.getUser().getUsername().equals(user.getUsername())) {
            throw new ForbiddenAccessException("Can't update comment from other user");
        }

        commentToUpdate.setText(updatedCommentDTO.getText());
        commentToUpdate.setModifiedAt(LocalDateTime.now());
        commentRepository.saveData(Constants.FILE_COMMENTS_HEADER);
        return commentToUpdate;
    }


    public void deleteCommentById(long commentId, User user) {
        Comment comment = this.commentRepository.getById(commentId);
        if (comment == null) {
            throw new BadRequestException("Can't find comment with id: " + commentId);
        }

        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            throw new ForbiddenAccessException("Can't delete comment from other user");
        }

        comment.setDeleted(true);
        commentRepository.saveData(Constants.FILE_COMMENTS_HEADER);
    }
}
