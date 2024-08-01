package me.litzrsh.consbits.app.user.service;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.authority.event.AuthoritySingleDeleteEvent;
import me.litzrsh.consbits.app.authority.event.AuthoritySingleSaveEvent;
import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.UserAuthority;
import me.litzrsh.consbits.app.user.event.UserListGetEvent;
import me.litzrsh.consbits.app.user.event.UserSingleDeleteEvent;
import me.litzrsh.consbits.app.user.event.UserSingleGetEvent;
import me.litzrsh.consbits.app.user.event.UserSingleSaveEvent;
import me.litzrsh.consbits.app.user.repository.UserAuthorityRepository;
import me.litzrsh.consbits.core.util.SessionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 사용자 권한 관리를 위한 서비스 클래스입니다.
 * UserAuthorityRepository를 통해 사용자 권한을 설정, 저장 및 삭제하는 메서드를 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class UserAuthorityService {

    private final UserAuthorityRepository userAuthorityRepository;

    /**
     * 사용자가 조회될 때 발생하는 이벤트를 처리합니다.
     * UserListGetEvent와 UserSingleGetEvent를 수신하여 사용자 권한을 설정합니다.
     *
     * @param event 사용자 조회 이벤트.
     */
    @EventListener({UserListGetEvent.class, UserSingleGetEvent.class})
    public void getEventHandler(ApplicationEvent event) {
        if (event instanceof UserListGetEvent e) {
            // 다수의 사용자에 대해 권한을 설정합니다.
            List<User> users = e.getEntities();
            userAuthorityRepository.setAllUserAuthorities(users);
        } else if (event instanceof UserSingleGetEvent e) {
            // 단일 사용자에 대해 권한을 설정합니다.
            User user = e.getEntity();
            user.setAuthorities(userAuthorityRepository.findAllByUsername(user.getUsername()));
        }
    }

    /**
     * 사용자가 저장될 때 발생하는 이벤트를 처리합니다.
     * 사용자에 대한 기존 권한을 삭제하고, 새로운 권한을 저장합니다.
     *
     * @param event 저장 이벤트.
     */
    @Transactional
    @EventListener({UserSingleSaveEvent.class, AuthoritySingleSaveEvent.class})
    public void saveEventHandler(ApplicationEvent event) {
        if (event instanceof UserSingleSaveEvent e) {
            User user = e.getEntity();
            // 사용자에 대한 기존 권한을 삭제합니다.
            userAuthorityRepository.deleteAllByUser(user);
            // 새로운 권한이 없으면 반환합니다.
            if (CollectionUtils.isEmpty(user.getAuthorities())) return;
            // 새로운 권한을 저장합니다.
            userAuthorityRepository.saveAll(user.getAuthorities().stream()
                    .map(a -> new UserAuthority(user.getId(), a.getId()))
                    .toList());
        } else if (event instanceof AuthoritySingleSaveEvent e) {
            Authority authority = e.getEntity();
            UserAuthority ua = userAuthorityRepository
                    .findById(new UserAuthority.Key(SessionUtils.getUsername(), authority.getId()))
                    .orElse(new UserAuthority(SessionUtils.getUsername(), authority.getId()));
            userAuthorityRepository.save(ua);
        }
    }

    /**
     * 사용자가 삭제될 때 발생하는 이벤트를 처리합니다.
     * 사용자에 대한 모든 권한을 삭제합니다.
     *
     * @param event 삭제 이벤트.
     */
    @Transactional
    @EventListener({UserSingleDeleteEvent.class, AuthoritySingleDeleteEvent.class})
    public void deleteEventHandler(ApplicationEvent event) {
        if (event instanceof UserSingleDeleteEvent e) {
            User user = e.getEntity();
            // 사용자에 대한 모든 권한을 삭제합니다.
            userAuthorityRepository.deleteAllByUser(user);
        } else if (event instanceof AuthoritySingleDeleteEvent e) {
            Authority authority = e.getEntity();
            // 권한에 대한 모든 사용자를 삭제합니다.
            userAuthorityRepository.deleteAllByAuthority(authority);
        }
    }
}
