package services;

import dto.NewUserDTO;
import exceptions.BadRequestException;
import model.Role;
import model.User;
import repository.UserRepository;
import util.Constants;
import validation.ValidationService;

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


}
