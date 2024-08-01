package me.litzrsh.consbits;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.authority.AuthorityMenu;
import me.litzrsh.consbits.app.authority.repository.AuthorityMenuRepository;
import me.litzrsh.consbits.app.authority.repository.AuthorityRepository;
import me.litzrsh.consbits.app.menu.Menu;
import me.litzrsh.consbits.app.menu.repository.MenuRepository;
import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.UserAuthority;
import me.litzrsh.consbits.app.user.repository.UserAuthorityRepository;
import me.litzrsh.consbits.app.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static me.litzrsh.consbits.core.CommonConstants.*;

@SpringBootTest
@ActiveProfiles("local")
class ApiApplicationTests {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private AuthorityMenuRepository authorityMenuRepository;

    @Test
    void contextLoads() {
        Authority authority = authorityRepository.findById(SYS_ADMIN).orElse(null);
        if (authority == null) {
            authority = Authority.builder()
                    .id(SYS_ADMIN)
                    .code(SYS_ADMIN)
                    .name("시스템관리자")
                    .remarks("시스템관리자 권한입니다.")
                    .use(true)
                    .build();
            authorityRepository.save(authority);
        }

        User user = userRepository.findById(SYS_ADMIN).orElse(null);
        if (user == null) {
            user = User.builder()
                    .id(SYS_ADMIN)
                    .type(USER_TYPE_BACKOFFICE)
                    .loginId("skyware@skyware.co.kr")
                    .password(passwordEncoder.encode("R2TnA9EfrxzvjVp8"))
                    .name("스카이웨어")
                    .email("skyware@skyware.co.kr")
                    .emailVerified(false)
                    .mobileVerified(false)
                    .status(USER_STATUS_NORMAL)
                    .joinAt(new Date())
                    .joinType(ADMIN)
                    .joinPath(WEB)
                    .build();
            userRepository.save(user);
            userAuthorityRepository.save(new UserAuthority(user.getId(), authority.getId()));
        }

        Menu ms = menuRepository.findById("MS0001").orElse(null);
        if (ms == null) {
            ms = Menu.builder()
                    .id("MS0001")
                    .type(MENU_TYPE_DIR)
                    .label("시스템")
                    .sort(9999)
                    .use(true)
                    .display(true)
                    .level(0)
                    .path("/")
                    .build();
            menuRepository.save(ms);
            authorityMenuRepository.save(new AuthorityMenu(authority.getId(), ms.getId(), 0xFF));
        }

        Menu mc = menuRepository.findById("MS1001").orElse(null);
        if (mc == null) {
            mc = Menu.builder()
                    .id("MS1001")
                    .parentId("MS0001")
                    .type(MENU_TYPE_PAGE)
                    .label("공통코드관리")
                    .link("/admin/code")
                    .sort(1000)
                    .use(true)
                    .display(true)
                    .level(1)
                    .path("/MS0001")
                    .build();
            menuRepository.save(mc);
            authorityMenuRepository.save(new AuthorityMenu(authority.getId(), mc.getId(), 0xFF));
        }

        Menu mm = menuRepository.findById("MS1002").orElse(null);
        if (mm == null) {
            mm = Menu.builder()
                    .id("MS1002")
                    .parentId("MS0001")
                    .type(MENU_TYPE_PAGE)
                    .label("메뉴관리")
                    .link("/admin/menu")
                    .sort(2000)
                    .use(true)
                    .display(true)
                    .level(1)
                    .path("/MS0001")
                    .build();
            menuRepository.save(mm);
            authorityMenuRepository.save(new AuthorityMenu(authority.getId(), mm.getId(), 0xFF));
        }

        Menu ma = menuRepository.findById("MS1003").orElse(null);
        if (ma == null) {
            ma = Menu.builder()
                    .id("MS1003")
                    .parentId("MS0001")
                    .type(MENU_TYPE_PAGE)
                    .label("권한관리")
                    .link("/admin/authority")
                    .sort(3000)
                    .use(true)
                    .display(true)
                    .level(1)
                    .path("/MS0001")
                    .build();
            menuRepository.save(ma);
            authorityMenuRepository.save(new AuthorityMenu(authority.getId(), ma.getId(), 0xFF));
        }

        Menu mu = menuRepository.findById("MS1004").orElse(null);
        if (mu == null) {
            mu = Menu.builder()
                    .id("MS1004")
                    .parentId("MS0001")
                    .type(MENU_TYPE_PAGE)
                    .label("사용자관리")
                    .link("/admin/user")
                    .sort(4000)
                    .use(true)
                    .display(true)
                    .level(1)
                    .path("/MS0001")
                    .build();
            menuRepository.save(mu);
            authorityMenuRepository.save(new AuthorityMenu(authority.getId(), mu.getId(), 0xFF));
        }
    }
}
