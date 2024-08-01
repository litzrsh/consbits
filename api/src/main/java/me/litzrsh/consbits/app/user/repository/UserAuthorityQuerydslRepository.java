package me.litzrsh.consbits.app.user.repository;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.user.User;

import java.util.List;

/**
 * QueryDSL을 사용하여 사용자 권한을 관리하기 위한 리포지토리 인터페이스입니다.
 */
public interface UserAuthorityQuerydslRepository {

    /**
     * 제공된 모든 사용자에 대해 권한을 설정합니다.
     *
     * @param users 권한을 설정할 사용자 목록.
     */
    void setAllUserAuthorities(List<User> users);

    /**
     * 사용자 이름과 연관된 모든 권한을 찾습니다.
     *
     * @param username 권한을 찾을 사용자 이름.
     * @return 사용자 이름과 연관된 권한 목록.
     */
    List<Authority> findAllByUsername(String username);

    /**
     * 특정 사용자와 연관된 모든 권한을 삭제합니다.
     *
     * @param user 권한을 삭제할 사용자.
     */
    void deleteAllByUser(User user);

    /**
     * 사용자 목록과 연관된 모든 권한을 삭제합니다.
     *
     * @param users 권한을 삭제할 사용자 목록.
     */
    void deleteAllByUsers(List<User> users);

    /**
     * 특정 권한과 연관된 모든 사용자 권한을 삭제합니다.
     *
     * @param authority 삭제할 권한.
     */
    void deleteAllByAuthority(Authority authority);

    /**
     * 권한 목록과 연관된 모든 사용자 권한을 삭제합니다.
     *
     * @param authorities 삭제할 권한 목록.
     */
    void deleteAllByAuthorities(List<Authority> authorities);
}
