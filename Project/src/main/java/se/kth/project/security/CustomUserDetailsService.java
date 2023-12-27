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

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findFirstByUsername(username);
        if(user != null) {
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
