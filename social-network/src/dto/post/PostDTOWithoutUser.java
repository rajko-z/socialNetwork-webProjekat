package dto.post;

import dto.comment.CommentDTO;
import lombok.*;
import model.PostType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTOWithoutUser {
    private Long id;
    private String text;
    private PostType type;
    private List<CommentDTO> comments;
    private LocalDateTime createdAt;
    private String imageUrl;
    private boolean isDeleted;
}
