package se.kth.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.kth.project.dto.RegistrationDTO;
import se.kth.project.dto.UserDTO;
import se.kth.project.model.UserEntity;
import se.kth.project.repository.UserRepository;
import se.kth.project.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;



    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(RegistrationDTO registrationDTO) {
        UserEntity user = new UserEntity();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
//        user.setPassword(registrationDTO.getPassword());
        user.setAdmin(registrationDTO.isAdmin());
//        user.setAdmin(true);
        userRepository.save(user);
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

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
