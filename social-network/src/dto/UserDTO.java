package dto;

import lombok.*;
import model.Gender;
import model.Role;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
    private String username;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private Gender gender;
    private boolean accountPrivate;
    private boolean isBlocked;
    private Role role;
    private String profileImageUrl;
}