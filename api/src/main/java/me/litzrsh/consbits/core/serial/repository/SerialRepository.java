package me.litzrsh.consbits.core.serial.repository;

import me.litzrsh.consbits.core.serial.Serial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SerialRepository extends JpaRepository<Serial, String> {
}
