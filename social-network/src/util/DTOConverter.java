package util;

import dto.UserDTO;
import model.User;

public class DTOConverter {

    public static UserDTO convertUserToDto(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getUsername())
                .accountPrivate(user.isAccountPrivate())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .isBlocked(user.isBlocked())
                .role(user.getRole())
                .build();
    }
}
