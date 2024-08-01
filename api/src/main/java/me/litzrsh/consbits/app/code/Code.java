package me.litzrsh.consbits.app.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.litzrsh.consbits.core.BaseModel;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.convert.BooleanStringConverter;
import me.litzrsh.consbits.core.convert.MapDataConverter;
import me.litzrsh.consbits.core.serial.SerialGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * 코드 엔티티 클래스.
 * 코드 정보를 담고 있으며, 데이터베이스와 매핑됩니다.
 */
@Entity
@Table(name = "CODE_BASE")
@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class Code extends BaseModel implements Comparable<Code> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    public static final SerialGenerator SERIAL = () -> "CODE";

    @Id
    @Column(name = "CODE_ID")
    private String id; // 코드 ID

    @Column(name = "CODE_UPPER_ID")
    private String parentId; // 상위 코드 ID

    @Column(name = "CODE_VAL")
    private String value; // 코드 값

    @Column(name = "CODE_NM")
    private String label; // 코드 이름

    @Column(name = "CODE_DC")
    private String remarks; // 코드 설명

    @Convert(converter = MapDataConverter.class)
    @Column(name = "CODE_DATA")
    private Map<String, Object> data = new HashMap<>(); // 코드 데이터

    @Convert(converter = BooleanStringConverter.class)
    @Column(name = "USE_YN")
    private Boolean use; // 사용 여부

    @Column(name = "SORT_SEQ")
    private Integer sort; // 정렬 순서

    @JsonIgnore
    @Column(name = "CODE_LVL")
    private Integer level; // 코드 레벨

    @Column(name = "CODE_PATH")
    private String path; // 코드 경로

    /**
     * 코드를 비교하여 정렬 순서에 따라 정렬합니다.
     *
     * @param o 비교할 코드 객체.
     * @return 비교 결과 값.
     */
    @Override
    public int compareTo(Code o) {
        return sort - o.sort;
    }
}
