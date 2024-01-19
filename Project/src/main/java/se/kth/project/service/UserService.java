package se.kth.project.service;

import se.kth.project.dto.RegistrationDTO;
import se.kth.project.dto.UserDTO;
import se.kth.project.model.UserEntity;

import java.util.List;

public interface UserService {
    List<UserDTO> retrieveAllStudents();
    UserDTO retrieveUser(String username, String password);
    int saveUser(RegistrationDTO registrationDTO);
    UserEntity findByUsername(String username);

    void delete(Integer studentId);

    UserEntity findById(Integer studentId);

    void updateUser(UserEntity user);

    List<UserDTO> retrieveAllStudentsForCourseByListId(Integer listId);
}
