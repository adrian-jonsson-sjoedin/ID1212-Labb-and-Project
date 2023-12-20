package se.kth.project.service;

import se.kth.project.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> retrieveAllStudents();
    UserDTO retrieveUser(String username, String password);
}
