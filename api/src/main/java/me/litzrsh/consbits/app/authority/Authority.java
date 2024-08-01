package me.litzrsh.consbits.app.authority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.litzrsh.consbits.app.authority.dto.MenuDTO;
import me.litzrsh.consbits.core.BaseModel;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.convert.BooleanStringConverter;
import me.litzrsh.consbits.core.serial.SerialGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.List;

/**
 * 권한 엔티티 클래스.
 * 권한 정보를 담고 있으며, 데이터베이스와 매핑됩니다.
 */
@Entity
@Table(name = "AUTH_BASE")
@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority extends BaseModel implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    public static final SerialGenerator SERIAL = () -> "AUTH";

    @Id
    @Column(name = "AUTH_ID")
    private String id; // 권한 ID

    @Column(name = "TYPE_CD")
    private String code; // 권한 유형 코드

    @Column(name = "AUTH_NM")
    private String name; // 권한 이름

    @Column(name = "AUTH_DC")
    private String remarks; // 권한 설명

    @Convert(converter = BooleanStringConverter.class)
    @Column(name = "USE_YN")
    private Boolean use; // 사용 여부

    @Transient
    private Integer userCount; // 권한에 맵핑된 사용자 수

    @Transient
    private List<MenuDTO> menus; // 권한 메뉴 목록

    @JsonIgnore
    @Override
    public String getAuthority() {
        return code;
    }
}
