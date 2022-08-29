package dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatedCommentDTO {
    @NotEmpty(message = "Comment should not be empty")
    @Size(min=1, max=1024, message = "Comment should be between 1 and 1024 chars")
    private String text;

    @NotNull(message = "Comment id should not be empty")
    private Long commentId;
}
