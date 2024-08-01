package me.litzrsh.consbits.core.util;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.repository.UserRepository;
import me.litzrsh.consbits.core.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"unused"})
@Slf4j
public abstract class SessionUtils {

    public static final String[] SYSTEM_ID_ARRAY = {CommonConstants.SYSTEM, CommonConstants.GUEST};

    public static final Map<String, String> NAME_MAP = new ConcurrentHashMap<>();

    private static UserRepository userRepository;

    public static User getUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof UsernamePasswordAuthenticationToken auth) {
                return (User) auth.getPrincipal();
            }
        } catch (Exception e) {
            if (log.isTraceEnabled()) e.printStackTrace(System.err);
        }
        return null;
    }

    public static String getUsername() {
        User user = getUser();
        return user == null ? CommonConstants.SYSTEM : user.getId();
    }

    public static List<String> getAuthorityIds() {
        User user = getUser();
        if (user == null) return List.of();
        return user.getAuthorities().stream().map(Authority::getId).toList();
    }

    public static boolean hasAuthority(String code) {
        User user = getUser();
        if (user == null) return false;
        return user.getAuthorities().stream()
                .map(Authority::getCode)
                .anyMatch(e -> e.equals(code));
    }

    public static boolean isSysAdmin() {
        return hasAuthority(CommonConstants.SYS_ADMIN);
    }

    public static boolean isAdmin() {
        return hasAuthority(CommonConstants.ADMIN);
    }

    public static String getUsername(String id) {
        if (id == null) return "";
        for (String SYSTEM_ID : SYSTEM_ID_ARRAY) {
            if (SYSTEM_ID.equals(id)) {
                return MessageUtils.getMessage(SYSTEM_ID);
            }
        }
        String name = NAME_MAP.get(id);
        if (name == null) {
            userRepository.getUsername(id)
                    .ifPresent((n) -> NAME_MAP.put(id, n));
        }
        return NAME_MAP.get(id);
    }

    public static void setUserRepository(UserRepository userRepository) throws Exception {
        if (SessionUtils.userRepository != null) {
            throw new Exception("UserRepository already been set!");
        }
        SessionUtils.userRepository = userRepository;
    }
}
