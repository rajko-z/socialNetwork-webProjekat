package dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FriendRequestDTO {
    private UserDTO from;
    private UserDTO to;
    private LocalDateTime createdAt;
}
