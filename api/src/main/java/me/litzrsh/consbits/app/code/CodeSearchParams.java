package me.litzrsh.consbits.app.code;

import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.SearchParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 코드 검색 조건을 담는 데이터 전송 객체 (DTO) 클래스.
 * 부모 ID, 경로, 검색어를 포함합니다.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CodeSearchParams extends SearchParams {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private String parentId; // 부모 코드 ID
    private String path; // 코드 경로
    private String query; // 검색어
}
