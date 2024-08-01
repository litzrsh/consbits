package me.litzrsh.consbits.app.code.service;

import me.litzrsh.consbits.app.code.Code;
import me.litzrsh.consbits.app.code.CodeSearchParams;
import me.litzrsh.consbits.app.code.repository.CodeRepository;
import me.litzrsh.consbits.core.Page;
import me.litzrsh.consbits.core.annotation.HasAuthority;
import me.litzrsh.consbits.core.exception.RestfulException;
import me.litzrsh.consbits.core.util.CodeUtils;
import me.litzrsh.consbits.core.util.RestUtils;
import me.litzrsh.consbits.core.util.SerialUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Queue;

import static me.litzrsh.consbits.core.CommonConstants.*;

/**
 * 관리자용 코드 서비스를 제공하는 클래스.
 * 코드에 대한 CRUD 작업을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@HasAuthority({SYS_ADMIN, ADMIN})
public class CodeAdminService {

    // 코드가 존재하지 않을 때 발생하는 예외
    public static final RestfulException NOT_FOUND = new RestfulException(HttpStatus.NOT_FOUND, "CE0001");
    // 부모 코드가 존재하지 않을 때 발생하는 예외
    public static final RestfulException PARENT_NOT_FOUND = new RestfulException(HttpStatus.NOT_FOUND, "CE0002");
    // 코드 값이 비어있을 때 발생하는 예외
    public static final RestfulException EMPTY_CODE = new RestfulException(HttpStatus.BAD_REQUEST, "CE0003");
    // 저장할 데이터가 없을 때 발생하는 예외
    public static final RestfulException EMPTY_LIST = new RestfulException(HttpStatus.BAD_REQUEST, "CE0004");
    // 코드가 중복되었을 대 발생하는 예외
    public static final RestfulException DUPLICATE_CODE = new RestfulException(HttpStatus.BAD_REQUEST, "CE0005");

    private final CodeRepository codeRepository;

    /**
     * 모든 코드를 조회합니다.
     *
     * @param params 코드 검색 조건이 담긴 객체.
     * @return 검색된 코드 리스트를 포함하는 페이지 객체.
     */
    public Page<Code> findAll(CodeSearchParams params) {
        return codeRepository.findByPage(params);
    }

    /**
     * ID로 코드를 조회합니다.
     *
     * @param id 조회할 코드의 ID.
     * @return 조회된 코드 객체.
     * @throws RestfulException 코드를 찾을 수 없는 경우 예외를 던집니다.
     */
    public Code findById(String id) throws RestfulException {
        return codeRepository.findById(id)
                .orElseThrow(() -> NOT_FOUND);
    }

    /**
     * 코드를 저장합니다. 새로운 코드를 생성하거나 기존 코드를 업데이트합니다.
     *
     * @param id   저장할 코드의 ID. 새 코드인 경우 null.
     * @param code 저장할 코드 정보가 담긴 객체.
     * @return 저장된 코드 객체.
     * @throws RestfulException 저장 과정에서 문제가 발생한 경우 예외를 던집니다.
     */
    @Transactional
    public Code save(String id, Code code) throws RestfulException {
        final boolean isEdit = StringUtils.hasText(id);
        Code parent = null;
        if (isEdit) {
            // 기존 코드를 업데이트하는 경우
            Code exists = codeRepository.findById(id).orElseThrow(() -> NOT_FOUND);
            code.setId(exists.getId());
            code.setParentId(exists.getParentId());
            code.setLevel(exists.getLevel());
            code.setPath(exists.getPath());
        } else {
            if (codeRepository.exists(code.getValue(), code.getParentId())) {
                throw DUPLICATE_CODE;
            }
            // 새로운 코드를 생성하는 경우
            code.setId(SerialUtils.get(Code.SERIAL));
            if (StringUtils.hasText(code.getParentId())) {
                parent = codeRepository.findById(code.getParentId()).orElseThrow(() -> PARENT_NOT_FOUND);
            } else {
                code.setLevel(0);
                code.setPath(PATH_DELIM);
            }
        }
        // 코드 값을 포맷팅하고 비어 있는지 확인
        code.setValue(CodeUtils.format(code.getValue()));
        if (code.getValue().isEmpty()) {
            throw EMPTY_CODE;
        }
        // 부모 코드가 있는 경우 계층 구조 설정
        if (parent != null) {
            code.setLevel(parent.getLevel() + 1);
            code.setPath(RestUtils.append(parent.getPath(), parent.getValue()));
        }
        return codeRepository.save(code);
    }

    @Deprecated
    @Transactional
    public void saveList(List<Code> entities) throws RestfulException {
        if (CollectionUtils.isEmpty(entities)) {
            throw EMPTY_LIST;
        }
        for (Code entity : entities) {
            String code = CodeUtils.format(entity.getValue());
            if (code.isEmpty()) {
                throw EMPTY_CODE;
            }
            entity.setValue(code);
        }
        codeRepository.saveAll(entities);
    }

    /**
     * 코드를 삭제합니다.
     *
     * @param id 삭제할 코드의 ID.
     * @throws RestfulException 코드를 찾을 수 없는 경우 예외를 던집니다.
     */
    @Transactional
    public void delete(String id) throws RestfulException {
        Code code = codeRepository.findById(id).orElseThrow(() -> NOT_FOUND);
        codeRepository.deleteWithChildren(code);
    }
}
