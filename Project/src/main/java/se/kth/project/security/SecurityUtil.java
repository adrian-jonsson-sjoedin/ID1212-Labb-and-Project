package se.kth.project.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for handling security-related operations.
 * <p>
 * This class provides a static method to retrieve the role of the currently authenticated user
 * from the security context.
 */
public class SecurityUtil {

    /**
     * Retrieves the role of the currently authenticated user from the security context.
     *
     * @return The role, Admin or Student, of the authenticated user, or {@code null} if the user is not authenticated.
     */
    public static String getSessionUserRole() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Assuming the user has only one authority/role. If there are multiple roles, you may need to handle accordingly.
            GrantedAuthority authority = authentication.getAuthorities().stream().findFirst().orElse(null);

            if (authority != null) {
                return authority.getAuthority();
            }
        }
        return null;
    }
    public static String getSessionUser() {
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }
    public static boolean isUserAdmin(HttpSession session) {
        String userRole = session.getAttribute("userRole").toString().toLowerCase();
        return userRole.equals("admin");
    }
}
