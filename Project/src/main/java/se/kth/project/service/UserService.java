package se.kth.project.service;

import se.kth.project.dto.RegistrationDTO;
import se.kth.project.dto.UserDTO;
import se.kth.project.model.UserEntity;
import se.kth.project.security.SecurityUtil;
import se.kth.project.service.impl.UserServiceImpl;

import java.util.List;
/**
 * Service interface defining operations related to user management.
 * This interface declares methods for saving users, retrieving user information,
 * deleting users, and converting user entities to data transfer objects.
 *
 * @see UserServiceImpl
 */
public interface UserService {

    /**
     * Retrieves all users in the db that have admin set to false, i.e., all students in the db.
     *
     * @return a list containing {@link UserDTO} objects
     */
    List<UserDTO> retrieveAllStudents();

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
    int saveUser(RegistrationDTO registrationDTO);

    /**
     * Retrieves a user entity from the database based on the given username.
     * <p>
     * This method queries the underlying {@code userRepository} to find a user entity by the specified username.
     *
     * @param username The username of the user to be retrieved.
     * @return The user entity associated with the provided username, or {@code null} if no user is found.
     */
    UserEntity findByUsername(String username);

    /**
     * Converts a {@link UserEntity} to a {@link UserDTO}.
     *
     * @param user The {@link UserEntity} to convert.
     * @return The corresponding {@link UserDTO}.
     */
    UserDTO convertToDTO(UserEntity user);

    /**
     * Deletes a user from the database based on the provided studentId.
     * This method is transactional, and it deletes related reservations and course accesses.
     *
     * @param studentId The ID of the student to be deleted.
     */
    void delete(Integer studentId);

    /**
     * Retrieves a user entity from the database based on the given studentId.
     *
     * @param studentId The ID of the student to be retrieved.
     * @return The user entity associated with the provided studentId, or {@code null} if no user is found.
     */
    UserEntity findById(Integer studentId);

    /**
     * Updates the information of a user in the database.
     *
     * @param user The updated {@link UserEntity} object.
     */
    void updateUser(UserEntity user);

    /**
     * Retrieves all students for a specific course based on the given listId.
     *
     * @param listId The ID of the list associated with the course.
     * @return a list containing {@link UserDTO} objects.
     */
    List<UserDTO> retrieveAllStudentsForCourseByListId(Integer listId);
}
