package util;

import dto.CommentDTO;
import dto.UserDTO;
import model.Comment;
import model.User;
import repository.RepoFactory;
import services.UserService;

import java.util.List;
import java.util.stream.Collectors;

public class DTOConverter {

    private static final UserService userService = new UserService(RepoFactory.userRepo);

    public static UserDTO convertUserToDto(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getUsername())
                .accountPrivate(user.isAccountPrivate())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .profileImageUrl(userService.getProfileImageUrlForUser(user))
                .isBlocked(user.isBlocked())
                .role(user.getRole())
                .build();
    }

    public static List<CommentDTO> convertListOfCommentsToDTOs(List<Comment> comments) {
        return comments.stream().map(c ->
                    CommentDTO.builder()
                            .user(convertUserToDto(c.getUser()))
                            .createdAt(c.getCreatedAt())
                            .isDeleted(c.isDeleted())
                            .modifiedAt(c.getModifiedAt())
                            .text(c.getText())
                            .build()
                ).collect(Collectors.toList());
    }
}