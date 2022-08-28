package util;

import dto.UserDTO;
import model.User;
import repository.RepoFactory;
import services.UserService;

public class DTOConverter {

    private static final UserService userService = new UserService(RepoFactory.userRepo);

    public static UserDTO convertUserToDto(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getUsername())
                .accountPrivate(user.isAccountPrivate())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .profileImageUrl(userService.getProfileImageUrlForUser(user))
                .isBlocked(user.isBlocked())
                .role(user.getRole())
                .build();
    }
}
