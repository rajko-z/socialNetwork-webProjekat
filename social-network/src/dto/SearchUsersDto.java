package dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchUsersDto {

  private String name;

    private String surname;

    private String sortBy;


  private LocalDate dateOfBirthMin;

  private LocalDate dateOfBirthMax;


}
