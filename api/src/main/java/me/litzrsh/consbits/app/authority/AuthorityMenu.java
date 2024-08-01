package me.litzrsh.consbits.app.authority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.litzrsh.consbits.core.BaseModel;
import me.litzrsh.consbits.core.CommonConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 권한-메뉴 관계를 나타내는 엔티티 클래스.
 * 권한과 메뉴의 관계를 정의하며, 데이터베이스와 매핑됩니다.
 */
@Entity
@Table(name = "AUTH_MENU")
@Data
@EqualsAndHashCode(callSuper = false, of = {"authorityId", "menuId"})
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityMenu extends BaseModel {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    /**
     * 복합 키를 사용하여 권한-메뉴 관계를 정의합니다.
     */
    @EmbeddedId
    @JsonIgnore
    @AttributeOverrides({
            @AttributeOverride(name = "authorityId", column = @Column(name = "AUTH_ID")),
            @AttributeOverride(name = "menuId", column = @Column(name = "MENU_ID"))
    })
    private Key key = new Key(); // 권한-메뉴 복합 키

    @Column(name = "AUTH_ID", insertable = false, updatable = false)
    private String authorityId; // 권한 ID

    @Column(name = "MENU_ID", insertable = false, updatable = false)
    private String menuId; // 메뉴 ID

    @JsonIgnore
    @Column(name = "AUTH_VAL")
    private Integer authorityValue; // 권한 값

    @Transient
    private List<MenuAuthority> authorities = new ArrayList<>(); // 권한 목록

    /**
     * 권한-메뉴 관계를 생성하는 생성자.
     *
     * @param authorityId 권한 ID.
     * @param menuId      메뉴 ID.
     * @param authorityValue 권한 값.
     */
    public AuthorityMenu(String authorityId, String menuId, Integer authorityValue) {
        this.key = new Key(authorityId, menuId);
        this.authorityId = authorityId;
        this.menuId = menuId;
        this.authorityValue = authorityValue;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
        this.key.setAuthorityId(authorityId);
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
        this.key.setMenuId(menuId);
    }

    public void setAuthorityValue(Integer authorityValue) {
        this.authorityValue = authorityValue;
        List<MenuAuthority> authorities = new ArrayList<>();
        for (MenuAuthority authority : MenuAuthority.values()) {
            if ((authorityValue & authority.getValue()) == authority.getValue()) {
                authorities.add(authority);
            }
        }
        this.authorities = authorities;
    }

    public List<MenuAuthority> getAuthorities() {
        List<MenuAuthority> authorities = new ArrayList<>();
        for (MenuAuthority authority : MenuAuthority.values()) {
            if ((authorityValue & authority.getValue()) == authority.getValue()) {
                authorities.add(authority);
            }
        }
        this.authorities = authorities;
        return authorities;
    }

    public void setAuthorities(List<MenuAuthority> authorities) {
        this.authorityValue = 0;
        for (MenuAuthority authority : authorities) {
            this.authorityValue = this.authorityValue | authority.getValue();
        }
        this.authorities = authorities;
    }

    /**
     * 권한-메뉴 복합 키를 나타내는 임베디드 클래스.
     */
    @Embeddable
    @Data
    @EqualsAndHashCode(of = {"authorityId", "menuId"})
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {

        @Serial
        private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

        private String authorityId; // 권한 ID
        private String menuId; // 메뉴 ID
    }
}
