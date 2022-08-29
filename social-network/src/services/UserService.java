package services;

import dto.ChangePasswordRequestDTO;
import dto.NewUserDTO;
import dto.UpdatedUserDTO;
import exceptions.BadRequestException;
import model.Post;
import model.PostType;
import model.Role;
import model.User;
import repository.UserRepository;
import util.Constants;
import validation.ValidationService;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final UserRepository userRepository;
    private final ValidationService validationService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validationService = new ValidationService();
    }

    public void registerNewUser(NewUserDTO newUserDTO) {
        String errorMsg = validationService.validateAndGetErrorMessage(newUserDTO);
        if (errorMsg != null) {
            throw new BadRequestException(errorMsg);
        }
        if (userAlreadyExist(newUserDTO.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        User newUser = new User(newUserDTO.getUsername(), newUserDTO.getPassword(),
                newUserDTO.getName(), newUserDTO.getSurname(), newUserDTO.getDateOfBirth(),
                newUserDTO.getGender(), newUserDTO.isAccountPrivate(), false, Role.REGULAR);
        this.userRepository.add(newUser);
        this.userRepository.saveData(Constants.FILE_USERS_HEADER);
    }

    public boolean userAlreadyExist(String username) {
        return userRepository.getByUsername(username) != null;
    }

    public User getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userRepository.getByUsernameAndPassword(username, password);
    }


    public String getProfileImageUrlForUser(User user) {
        if (user.hasProfileImage()) {
            String imageName = user.getProfileImage().getImage().getName();
            return Constants.IMAGE_URL_PATH + imageName;
        }
        return null;
    }

    public List<Post> getPostsFromUserByPostType(User user, PostType postType) {
        return user.getUndeletedPosts().stream()
                .filter(p -> p.getType().equals(postType)).collect(Collectors.toList());
    }

    public User updateUser(UpdatedUserDTO u) {
        String errorMsg = validationService.validateAndGetErrorMessage(u);
        if (errorMsg != null) {
            throw new BadRequestException(errorMsg);
        }
        User foundUser = getUserByUsername(u.getUsername());
        foundUser.setName(u.getName());
        foundUser.setSurname(u.getUsername());
        foundUser.setAccountPrivate(u.isAccountPrivate());
        foundUser.setDateOfBirth(u.getDateOfBirth());
        foundUser.setGender(u.getGender());
        userRepository.saveData(Constants.FILE_USERS_HEADER);
        return foundUser;
    }

    public void updatePassword(ChangePasswordRequestDTO c) {
        User foundUser = getUserByUsername(c.getUsername());

        if (!foundUser.getPassword().equals(c.getCurrentPassword()))
            throw new BadRequestException("Invalid current password");

        String errorMsg = validationService.validateAndGetErrorMessage(c);
        if (errorMsg != null) {
            throw new BadRequestException(errorMsg);
        }

        foundUser.setPassword(c.getNewPassword());
        userRepository.saveData(Constants.FILE_USERS_HEADER);
    }

    public void getAllPostsWithCommentsForUser(User user) {

    }
}
