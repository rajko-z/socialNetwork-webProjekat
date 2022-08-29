package dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private UserDTO user;
    private boolean isDeleted;
}
