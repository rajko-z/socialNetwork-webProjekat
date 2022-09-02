package dto.message;

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
public class NewMessageDTO {
    @Size(max = 20, message = "Username content cant be greater than 20 chars")
    private String username;
    @NotNull(message = "Invalid message, it cant be null")
    @Size(max = 2048, message = "Message content cant be greater than 2048 chars")
    private String text;
}