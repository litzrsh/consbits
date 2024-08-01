package me.litzrsh.consbits.app.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.core.BaseModel;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.convert.BooleanStringConverter;
import me.litzrsh.consbits.core.serial.SerialGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USER_BASE")
@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseModel implements UserDetails {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    public static final SerialGenerator SERIAL = () -> "USER";

    @Id
    @Column(name = "USER_ID")
    private String id;

    @Column(name = "TYPE_CD")
    private String type;

    @Column(name = "LOGIN_ID")
    private String loginId;

    @JsonIgnore
    @Column(name = "LOGIN_PWD")
    private String password;

    @Column(name = "USER_NM")
    private String name;

    @Column(name = "USER_DC")
    private String remarks;

    @Column(name = "IMG_PATH")
    private String imageUrl;

    @Column(name = "USER_EMAIL")
    private String email;

    @Convert(converter = BooleanStringConverter.class)
    @Column(name = "USER_EMAIL_CERT_YN")
    private Boolean emailVerified;

    @Column(name = "USER_EMAIL_CERT_DTTM")
    private Date emailVerifiedAt;

    @Column(name = "USER_MOBILE")
    private String mobile;

    @Convert(converter = BooleanStringConverter.class)
    @Column(name = "USER_MOBILE_CERT_YN")
    private Boolean mobileVerified;

    @Column(name = "USER_MOBILE_CERT_DTTM")
    private Date mobileVerifiedAt;

    @Column(name = "STTUS_CD")
    private String status;

    @Column(name = "JOIN_DTTM")
    private Date joinAt;

    @Column(name = "JOIN_TYPE_CD")
    private String joinType;

    @Column(name = "JOIN_PATH_CD")
    private String joinPath;

    @Column(name = "WHDWL_DTTM")
    private Date withdrawAt;

    @Column(name = "WHDWL_RSN_CD")
    private String withdrawReasonCode;

    @Column(name = "WHDWL_RSN_CN")
    private String withdrawReason;

    @Column(name = "LAST_LOGIN_DTTM")
    private Date lastLoginAt;

    @Column(name = "LOGIN_FAIL_CNT")
    private Integer loginFailCount;

    @Column(name = "LAST_CHG_PWD_DTTM")
    private Date lastChangePasswordAt;

    @Transient
    private List<Authority> authorities = new ArrayList<>();

    @JsonIgnore
    @Override
    public String getUsername() {
        return id;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void cleanup() {
        password = null;
        name = null;
        remarks = null;
        imageUrl = null;
    }
}
