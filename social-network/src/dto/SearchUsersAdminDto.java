package dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchUsersAdminDto {

    private String name;

    private String surname;

    private String username;

    private String sortBy;



}
