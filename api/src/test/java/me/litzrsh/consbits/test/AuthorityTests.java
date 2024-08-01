package me.litzrsh.consbits.test;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.authority.repository.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles({"local"})
@Slf4j
public class AuthorityTests {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void test() {
        List<Authority> authorities = authorityRepository.findAuthorities();
        log.info("{}", authorities);
    }
}
