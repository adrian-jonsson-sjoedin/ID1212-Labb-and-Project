package se.kth.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.kth.project.dto.RegistrationDTO;
import se.kth.project.dto.UserDTO;
import se.kth.project.model.UserEntity;
import se.kth.project.repository.UserRepository;
import se.kth.project.security.SecurityUtil;
import se.kth.project.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a new instance of the {@code UserServiceImpl} class.
     *
     * @param userRepository  The repository for user-related database operations.
     * @param passwordEncoder The password encoder for securing user passwords.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a new user based on the provided registration information.
     * <p>
     * This method checks the role of the currently authenticated user using
     * {@link SecurityUtil#isUserAdmin()}. If the user has the role of "admin",
     * a new user is created and saved to the database with the provided registration details.
     * The password is encoded using the configured password encoder.
     *
     * @param registrationDTO The data transfer object containing user registration information.
     * @return 0 if the user is successfully saved by an admin; -1 otherwise.
     */
    @Override
    public int saveUser(RegistrationDTO registrationDTO) {
        if (SecurityUtil.isUserAdmin()) {
            UserEntity user = new UserEntity();
            user.setUsername(registrationDTO.getUsername());
            user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            user.setAdmin(registrationDTO.isAdmin());
            userRepository.save(user);
            return 0;
        }
        return -1;
    }

    /**
     * Retrieves the user with matching username and password from the db.
     *
     * @param username
     * @param password
     * @return a UserDTO object for the user
     */
    @Override
    public UserDTO retrieveUser(String username, String password) {
        Optional<UserEntity> userOptional = userRepository.findByUsernameAndPassword(username, password);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            return convertToUserDTO(user);
        } else {
            return null;
        }
    }


    /**
     * Retrieves all users in the db that have admin set to false, i.e., all students in the db.
     *
     * @return a list containing UserDTO objects
     */
    @Override
    public List<UserDTO> retrieveAllStudents() {
        List<UserEntity> users = userRepository.findAllByAdminIsFalse();
        return users.stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    private UserDTO convertToUserDTO(UserEntity user) {
        return UserDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .admin(user.isAdmin())
                .build();
    }

    /**
     * Retrieves a user entity from the database based on the given username.
     * <p>
     * This method queries the underlying {@code userRepository} to find a user entity by the specified username.
     *
     * @param username The username of the user to be retrieved.
     * @return The user entity associated with the provided username, or {@code null} if no user is found.
     */
    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void delete(Integer studentId) {
        userRepository.deleteById(studentId);
    }

    @Override
    public UserEntity findById(Integer studentId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(studentId);
        return userEntityOptional.orElse(null);
    }
}
