package dto.user;

import lombok.*;
import model.Gender;
import validation.AgeRestriction;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdatedUserDTO {
    @NotEmpty(message = "Username should not be empty")
    @Size(min=2, max=50, message = "Username should be between 2 and 50 chars")
    private String username;

    @NotEmpty(message = "Name should not be empty")
    @Size(min=2, max=50, message = "Name should be between 2 and 50 chars")
    private String name;

    @NotEmpty(message = "Surname should not be empty")
    @Size(min=2, max=50, message = "Surname should be between 2 and 50 chars")
    private String surname;

    @NotNull(message = "Date of Birth cant be null")
    @AgeRestriction
    private LocalDate dateOfBirth;

    @NotNull(message = "You are either boy or girl... Or not :D ")
    private Gender gender;

    @NotNull(message = "Info about account visibility should not be null")
    private boolean accountPrivate;
}
