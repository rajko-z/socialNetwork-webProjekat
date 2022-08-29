package dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.PostType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewPostDTO {
    @Size(max = 2048, message = "Post content cant be greater than 2048 chars")
    private String text;
    @NotNull(message = "Invalid post type, it cant be null")
    private PostType type;
    private String base64Image;
    @Size(max = 150, message = "Image name cant be greater then 150 chars")
    private String imageName;
}
