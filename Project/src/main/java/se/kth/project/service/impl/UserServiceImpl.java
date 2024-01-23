package se.kth.project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.kth.project.dto.RegistrationDTO;
import se.kth.project.dto.UserDTO;
import se.kth.project.model.ListEntity;
import se.kth.project.model.UserEntity;
import se.kth.project.repository.ListRepository;
import se.kth.project.repository.ReservationRepository;
import se.kth.project.repository.UserRepository;
import se.kth.project.security.SecurityUtil;
import se.kth.project.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link UserService} interface that provides
 * functionality for managing user-related operations in the application.
 * This service includes methods for saving users, retrieving user information,
 * deleting users, and converting user entities to data transfer objects.
 *
 * @see UserService
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ListRepository listRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new instance of the {@code UserServiceImpl} class.
     *
     * @param userRepository        The repository for user-related database operations.
     * @param listRepository        The repository for booking list-related database operations.
     * @param reservationRepository The repository for reservation-related database operations.
     * @param passwordEncoder       The password encoder for securing user passwords.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ListRepository listRepository,
                           ReservationRepository reservationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.listRepository = listRepository;
        this.reservationRepository = reservationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public void updateUser(UserEntity user) {
        userRepository.save(user);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserDTO> retrieveAllStudents() {
        List<UserEntity> users = userRepository.findAllByAdminIsFalse();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO convertToDTO(UserEntity user) {
        return UserDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .admin(user.isAdmin())
                .courses(user.getCourses())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserDTO> retrieveAllStudentsForCourseByListId(Integer listId) {
        List<UserEntity> allStudents = userRepository.findAllByAdminIsFalse();
        ListEntity list = listRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException("List not found"));
        Integer courseId = list.getCourse().getId();
        return allStudents.stream()
                .filter(student -> student.getCourses().stream().anyMatch(course -> course.getId().equals(courseId)))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(Integer studentId) {
        reservationRepository.deleteReservationsByUserId(studentId);
        userRepository.deleteCourseAccessByUserId(studentId);
        userRepository.deleteById(studentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity findById(Integer studentId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(studentId);
        return userEntityOptional.orElse(null);
    }
}
