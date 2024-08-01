package me.litzrsh.consbits.app.code.repository;

import me.litzrsh.consbits.app.code.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 코드 데이터를 관리하는 저장소 인터페이스.
 * JPA를 통해 코드 데이터를 저장하고 조회하는 기능을 제공합니다.
 */
@Repository
public interface CodeRepository extends CodeQuerydslRepository, JpaRepository<Code, String> {

    /**
     * 값과 경로를 기준으로 코드를 조회합니다.
     *
     * @param value 조회할 코드 값.
     * @param path 조회할 코드 경로.
     * @return 조회된 코드 객체.
     */
    Optional<Code> findByValueAndPath(String value, String path);

    /**
     * 특정 경로에 해당하는 모든 코드를 조회합니다.
     *
     * @param path 조회할 코드 경로.
     * @return 조회된 코드 리스트.
     */
    List<Code> findAllByPath(String path);
}
