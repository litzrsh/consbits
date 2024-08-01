package me.litzrsh.consbits.app.user.repository;

import me.litzrsh.consbits.app.user.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, String> {
}
