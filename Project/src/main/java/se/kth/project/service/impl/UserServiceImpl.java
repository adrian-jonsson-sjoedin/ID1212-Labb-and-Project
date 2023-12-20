package se.kth.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.project.dto.UserDTO;
import se.kth.project.model.User;
import se.kth.project.repository.UserRepository;
import se.kth.project.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        Optional<User> userOptional = userRepository.findByUsernameAndPassword(username, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
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
        List<User> users = userRepository.findAllByAdminIsFalse();
        return users.stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    private UserDTO convertToUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .admin(user.isAdmin())
                .build();
    }
}
