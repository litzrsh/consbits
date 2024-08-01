package me.litzrsh.consbits.app.menu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.litzrsh.consbits.core.BaseModel;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.Tree;
import me.litzrsh.consbits.core.convert.BooleanStringConverter;
import me.litzrsh.consbits.core.serial.SerialGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.List;

/**
 * 메뉴 엔티티 클래스.
 * 메뉴 정보를 담고 있으며, 데이터베이스와 매핑됩니다.
 */
@Entity
@Table(name = "MENU_BASE")
@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu extends BaseModel implements Tree<Menu> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    public static final SerialGenerator SERIAL = () -> "MENU";

    /**
     * 메뉴 ID.
     */
    @Id
    @Column(name = "MENU_ID")
    private String id;

    /**
     * 상위 메뉴 ID.
     */
    @Column(name = "MENU_UPPER_ID")
    private String parentId;

    /**
     * 메뉴 유형 코드.
     */
    @Column(name = "TYPE_CD")
    private String type;

    /**
     * 메뉴 이름.
     */
    @Column(name = "MENU_NM")
    private String label;

    /**
     * 메뉴 짧은 이름.
     */
    @Column(name = "MENU_NM_SHORT")
    private String shortLabel;

    /**
     * 메뉴 설명.
     */
    @Column(name = "MENU_DC")
    private String remarks;

    /**
     * 메뉴 링크.
     */
    @Column(name = "MENU_LINK")
    private String link;

    /**
     * 메뉴 아이콘.
     */
    @Column(name = "MENU_ICO")
    private String icon;

    /**
     * 정렬 순서.
     */
    @Column(name = "SORT_SEQ")
    private Integer sort;

    /**
     * 사용 여부.
     */
    @Convert(converter = BooleanStringConverter.class)
    @Column(name = "USE_YN")
    private Boolean use;

    /**
     * 표시 여부.
     */
    @Convert(converter = BooleanStringConverter.class)
    @Column(name = "DISP_YN")
    private Boolean display;

    /**
     * 메뉴 레벨.
     */
    @JsonIgnore
    @Column(name = "MENU_LVL")
    private int level;

    /**
     * 메뉴 경로.
     */
    @JsonIgnore
    @Column(name = "MENU_PATH")
    private String path;

    /**
     * 하위 메뉴 목록.
     */
    @Transient
    private List<Menu> children;
}
