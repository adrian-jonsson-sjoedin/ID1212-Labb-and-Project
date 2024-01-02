package se.kth.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.kth.project.model.UserEntity;
import se.kth.project.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * <p>
 * This service is responsible for loading user details during the authentication process.
 * It retrieves user information from the database and constructs a UserDetails object,
 * including user roles/authorities.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    /**
     * Constructs a new instance of the {@code CustomUserDetailsService} class.
     *
     * @param userRepository The repository for user-related database operations.
     */
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details based on the provided username.
     *
     * @param username The username of the user to be loaded.
     * @return A UserDetails object representing the authenticated user.
     * @throws UsernameNotFoundException If the user with the given username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findFirstByUsername(username);
        if (user != null) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            if (user.isAdmin()) {
                authorities.add(new SimpleGrantedAuthority("Admin"));
            } else {
                authorities.add(new SimpleGrantedAuthority("Student"));
            }
            User authUser = new User(
                    user.getUsername(),
                    user.getPassword(),
                    authorities
            );
            for (SimpleGrantedAuthority authority : authorities) {
                System.out.println(authority);
            }
            return authUser;
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
