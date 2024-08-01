package me.litzrsh.consbits.core.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import me.litzrsh.consbits.core.props.DatasourceConfigurationProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

import static me.litzrsh.consbits.core.CommonConstants.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DatasourceConfig implements InitializingBean {

    private final DataSource dataSource;

    private final DatasourceConfigurationProperties properties;

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final MenuRepository menuRepository;
    private final AuthorityMenuRepository authorityMenuRepository;

    @Transactional
    public void initialize() {
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
                    .loginId("litzrsh@gmail.com")
                    .password(passwordEncoder.encode("8TWpEkqUjVRgcHqS"))
                    .name("윤지영")
                    .email("litzrsh@gmail.com")
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

        Menu ms = menuRepository.findById("C202400000001").orElse(null);
        if (ms == null) {
            ms = Menu.builder()
                    .id("C202400000001")
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

        Menu mc = menuRepository.findById("C202400000002").orElse(null);
        if (mc == null) {
            mc = Menu.builder()
                    .id("C202400000002")
                    .parentId("C202400000001")
                    .type(MENU_TYPE_PAGE)
                    .label("공통코드관리")
                    .link("/admin/code")
                    .sort(1000)
                    .use(true)
                    .display(true)
                    .level(1)
                    .path("/C202400000001")
                    .build();
            menuRepository.save(mc);
            authorityMenuRepository.save(new AuthorityMenu(authority.getId(), mc.getId(), 0xFF));
        }

        Menu mm = menuRepository.findById("C202400000003").orElse(null);
        if (mm == null) {
            mm = Menu.builder()
                    .id("C202400000003")
                    .parentId("C202400000001")
                    .type(MENU_TYPE_PAGE)
                    .label("메뉴관리")
                    .link("/admin/menu")
                    .sort(2000)
                    .use(true)
                    .display(true)
                    .level(1)
                    .path("/C202400000001")
                    .build();
            menuRepository.save(mm);
            authorityMenuRepository.save(new AuthorityMenu(authority.getId(), mm.getId(), 0xFF));
        }

        Menu ma = menuRepository.findById("C202400000004").orElse(null);
        if (ma == null) {
            ma = Menu.builder()
                    .id("C202400000004")
                    .parentId("C202400000001")
                    .type(MENU_TYPE_PAGE)
                    .label("권한관리")
                    .link("/admin/authority")
                    .sort(3000)
                    .use(true)
                    .display(true)
                    .level(1)
                    .path("/C202400000001")
                    .build();
            menuRepository.save(ma);
            authorityMenuRepository.save(new AuthorityMenu(authority.getId(), ma.getId(), 0xFF));
        }

        Menu mu = menuRepository.findById("C202400000005").orElse(null);
        if (mu == null) {
            mu = Menu.builder()
                    .id("C202400000005")
                    .parentId("C202400000001")
                    .type(MENU_TYPE_PAGE)
                    .label("사용자관리")
                    .link("/admin/user")
                    .sort(4000)
                    .use(true)
                    .display(true)
                    .level(1)
                    .path("/C202400000001")
                    .build();
            menuRepository.save(mu);
            authorityMenuRepository.save(new AuthorityMenu(authority.getId(), mu.getId(), 0xFF));
        }
    }

    @Transactional
    @Override
    public void afterPropertiesSet() throws Exception {
        if (!properties.isInit()) return;

        try (Connection conn = dataSource.getConnection()) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("scheme-mariadb.sql")) {
                if (is == null) throw new Exception("Failed to read file");
                try (Scanner scanner = new Scanner(is).useDelimiter(";")) {
                    while (scanner.hasNext()) {
                        String query = scanner.next();
                        if (query.isEmpty()) continue;
                        try (Statement statement = conn.createStatement()) {
                            statement.execute(query);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
            conn.commit();
        }

        initialize();
    }
}
