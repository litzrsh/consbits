package me.litzrsh.consbits.core.serial.repository;

import jakarta.persistence.LockModeType;
import me.litzrsh.consbits.core.serial.SerialMutex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SerialMutexRepository extends JpaRepository<SerialMutex, String> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SerialMutex> findById(String id);
}
