package dto;

import lombok.*;
import model.Gender;
import model.Role;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithJWTResponse {
    private String username;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Role role;
    private boolean isAccountPrivate;
    private String jwt;
}
