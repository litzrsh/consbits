package me.litzrsh.consbits.app.authority.dto;

import me.litzrsh.consbits.app.authority.MenuAuthority;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.Tree;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 메뉴 데이터를 전달하기 위한 DTO 클래스.
 * 메뉴 정보와 트리 구조를 관리합니다.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO implements Serializable, Tree<MenuDTO> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private String id; // 메뉴 ID
    private String parentId; // 메뉴 상위 ID
    private String type; // 메뉴 유형 코드
    private String label; // 메뉴 이름
    private String shortLabel; // 메뉴 이름 (단축)
    private String remarks; // 메뉴 설명
    private String link; // 메뉴 링크
    private String icon; // 메뉴 아이콘
    private Integer sort; // 정렬 순번
    private Boolean use; // 사용 여부
    private Boolean display; // 전시 여부
    private List<MenuDTO> children; // 하위 메뉴 목록
    private Integer authorityValue = 0; // 권한 값 (int)
    private List<MenuAuthority> authorities = new ArrayList<>(); // 권한 목록
    private boolean open = false; // 메뉴 열림 여부
    private boolean active = false; // 메뉴 활성화 여부

    /**
     * 권한 값을 설정합니다. 권한 값을 기반으로 권한 목록을 업데이트합니다.
     *
     * @param authorityValue 설정할 권한 값.
     */
    public void setAuthorityValue(Integer authorityValue) {
        this.authorityValue = authorityValue;
        this.authorities = new ArrayList<>();
        for (MenuAuthority authority : MenuAuthority.values()) {
            if ((this.authorityValue & authority.getValue()) == authority.getValue()) {
                this.authorities.add(authority);
            }
        }
    }

    /**
     * 권한 목록을 반환합니다. 권한 값을 기반으로 권한 목록을 업데이트합니다.
     *
     * @return 권한 목록.
     */
    public List<MenuAuthority> getAuthorities() {
        this.authorities = new ArrayList<>();
        for (MenuAuthority authority : MenuAuthority.values()) {
            if ((this.authorityValue & authority.getValue()) == authority.getValue()) {
                this.authorities.add(authority);
            }
        }
        return this.authorities;
    }

    /**
     * 권한 목록을 설정합니다. 권한 목록을 기반으로 권한 값을 업데이트합니다.
     *
     * @param authorities 설정할 권한 목록.
     */
    public void setAuthorities(List<MenuAuthority> authorities) {
        this.authorities = authorities;
        this.authorityValue = 0;
        for (MenuAuthority authority : authorities) {
            this.authorityValue = this.authorityValue | authority.getValue();
        }
    }
}
