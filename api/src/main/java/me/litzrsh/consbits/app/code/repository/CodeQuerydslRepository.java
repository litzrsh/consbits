package me.litzrsh.consbits.app.code.repository;

import me.litzrsh.consbits.app.code.Code;
import me.litzrsh.consbits.app.code.CodeSearchParams;
import me.litzrsh.consbits.core.Page;

/**
 * QueryDSL을 사용한 코드 데이터의 커스텀 쿼리를 위한 저장소 인터페이스.
 * 이 인터페이스를 통해 JPA 저장소에서 제공하지 않는 복잡한 쿼리를 정의할 수 있습니다.
 */
public interface CodeQuerydslRepository {

    /**
     * 페이지별로 코드를 조회합니다.
     *
     * @param params 코드 검색 조건이 담긴 객체.
     * @return 검색된 코드 리스트를 포함하는 페이지 객체.
     */
    Page<Code> findByPage(CodeSearchParams params);

    /**
     * 하위 코드를 포함하여 코드를 삭제합니다.
     *
     * @param code 삭제할 코드 객체.
     */
    void deleteWithChildren(Code code);

    /**
     * 특정 값과 부모 ID를 가진 코드가 존재하는지 확인합니다.
     *
     * @param value 확인할 코드 값.
     * @param parentId 확인할 부모 ID.
     * @return 코드 존재 여부.
     */
    boolean exists(String value, String parentId);
}
