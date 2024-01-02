package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.project.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    List<UserEntity> findAllByAdminIsFalse();

    UserEntity findFirstByUsername(String username);

    UserEntity findByUsername(String userName);
}
