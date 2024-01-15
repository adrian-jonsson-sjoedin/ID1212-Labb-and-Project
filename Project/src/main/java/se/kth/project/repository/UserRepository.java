package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
}
