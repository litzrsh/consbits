package me.litzrsh.consbits.app.user.repository;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.UserSearchParams;
import me.litzrsh.consbits.core.Page;

import java.util.Optional;

public interface UserQuerydslRepository {

    Page<User> findByPages(UserSearchParams params);

    Optional<String> getUsername(String id);

    void addLoginFailCount(String id);

    void processAuthenticationSuccess(String id);
}
