package dto;

import lombok.*;
import validation.AgeRestriction;

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


  @AgeRestriction
  private LocalDate dateOfBirthMin;

  @AgeRestriction
  private LocalDate dateOfBirthMax;


}
