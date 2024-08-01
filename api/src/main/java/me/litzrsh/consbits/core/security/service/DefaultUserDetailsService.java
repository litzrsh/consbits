package me.litzrsh.consbits.core.security.service;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.event.UserSingleGetEvent;
import me.litzrsh.consbits.app.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService, UserDetailsPasswordService {

    private final UserRepository userRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        if (user instanceof User u) {
            u.setPassword(newPassword);
            userRepository.save(u);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        eventPublisher.publishEvent(new UserSingleGetEvent(user));
        return user;
    }
}
