package dto.message;


import dto.user.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DirectMessageDTO {
    private UserDTO from;
    private UserDTO to;
    private LocalDateTime createdAt;
    private String text;
    private boolean adminSent;
}
