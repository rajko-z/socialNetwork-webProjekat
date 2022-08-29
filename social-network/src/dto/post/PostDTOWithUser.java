package dto.post;

import dto.comment.CommentDTO;
import dto.user.UserDTO;
import lombok.*;
import model.PostType;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTOWithUser {
    private Long id;
    private String text;
    private PostType type;
    private UserDTO user;
    private List<CommentDTO> comments;
    private LocalDateTime createdAt;
    private String imageUrl;
    private boolean isDeleted;
}
