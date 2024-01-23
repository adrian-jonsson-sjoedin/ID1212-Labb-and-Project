package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import se.kth.project.model.UserEntity;

import java.util.List;

/**
 * Repository interface for managing {@link se.kth.project.model.UserEntity} entities.
 * Provides CRUD operations and custom queries for users.
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    /**
     * Retrieves a list of user entities where the admin column is set to false.
     *
     * @return A list of user entities where the admin flag is false.
     */
    List<UserEntity> findAllByAdminIsFalse();

    /**
     * Retrieves the first user entity with the specified username.
     *
     * @param username The username of the user to be retrieved.
     * @return The user entity with the specified username.
     */
    UserEntity findFirstByUsername(String username);

    /**
     * Retrieves the user entity with the specified username.
     *
     * @param userName The username of the user to be retrieved.
     * @return The user entity with the specified username.
     */
    UserEntity findByUsername(String userName);

    /**
     * Deletes course access for a user based on the user ID.
     *
     * @param userId The ID of the user for whom course access is to be deleted.
     */
    @Modifying
    @Query(value = "DELETE FROM course_access WHERE user_id = :userId", nativeQuery = true)
    void deleteCourseAccessByUserId(Integer userId);


//    @Query(value = "SELECT DISTINCT u.* " +
//            "FROM users u " +
//            "JOIN course_access ca ON u.id = ca.user_id " +
//            "JOIN courses c ON ca.course_id = c.id " +
//            "JOIN lists l ON c.id = l.course_id " +
//            "WHERE l.id = :listId", nativeQuery = true)
//    List<UserDTO> findAllForSpecificCourseByListId(@Param("listId") Integer listId);
}
