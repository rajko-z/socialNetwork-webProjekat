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
@ToString
public class ChangePasswordRequestDTO {
    @NotNull
    private String username;

    @NotEmpty
    private String currentPassword;

    @NotEmpty
    @Size(min=3, max=250, message = "Password should be between 3 and 250 chars")
    private String newPassword;
}
