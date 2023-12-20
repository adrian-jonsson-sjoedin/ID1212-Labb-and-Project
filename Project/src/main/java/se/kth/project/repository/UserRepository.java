package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.project.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndPassword(String username, String password);
    List<User> findAllByAdminIsFalse();
}
