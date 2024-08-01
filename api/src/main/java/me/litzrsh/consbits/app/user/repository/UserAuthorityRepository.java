package me.litzrsh.consbits.app.user.repository;

import me.litzrsh.consbits.app.user.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityRepository extends UserAuthorityQuerydslRepository, JpaRepository<UserAuthority, UserAuthority.Key> {
}
