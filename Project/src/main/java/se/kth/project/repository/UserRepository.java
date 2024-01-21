package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.kth.project.dto.UserDTO;
import se.kth.project.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    List<UserEntity> findAllByAdminIsFalse();

    UserEntity findFirstByUsername(String username);

    UserEntity findByUsername(String userName);
    @Modifying
    @Query(value = "DELETE FROM course_access WHERE user_id = :userId", nativeQuery = true)
    void deleteCourseAccessByUserId(Integer userId);


    @Query(value = "SELECT DISTINCT u.* " +
            "FROM users u " +
            "JOIN course_access ca ON u.id = ca.user_id " +
            "JOIN courses c ON ca.course_id = c.id " +
            "JOIN lists l ON c.id = l.course_id " +
            "WHERE l.id = :listId", nativeQuery = true)
    List<UserDTO> findAllForSpecificCourseByListId(@Param("listId") Integer listId);
}
