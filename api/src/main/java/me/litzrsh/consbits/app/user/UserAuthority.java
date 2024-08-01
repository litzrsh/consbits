package me.litzrsh.consbits.app.user;

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

@Entity
@Table(name = "USER_AUTH")
@Data
@EqualsAndHashCode(callSuper = false, of = {"userId", "authorityId"})
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthority extends BaseModel {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    @JsonIgnore
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "userId", column = @Column(name = "USER_ID")),
            @AttributeOverride(name = "authorityId", column = @Column(name = "AUTH_ID"))
    })
    private Key key = new Key();

    @Column(name = "USER_ID", insertable = false, updatable = false)
    private String userId;

    @Column(name = "AUTH_ID", insertable = false, updatable = false)
    private String authorityId;

    public UserAuthority(String userId, String authorityId) {
        this.key = new Key(userId, authorityId);
        this.userId = userId;
        this.authorityId = authorityId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        this.key.setUserId(userId);
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
        this.key.setAuthorityId(authorityId);
    }

    @Embeddable
    @Data
    @EqualsAndHashCode(of = {"userId", "authorityId"})
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {

        @Serial
        private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

        private String userId;
        private String authorityId;
    }
}
