package me.litzrsh.consbits.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import me.litzrsh.consbits.core.util.SessionUtils;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseModel implements Serializable {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    @Column(name = "REGIST_DTTM", updatable = false)
    private Date createAt;

    @JsonIgnore
    @Column(name = "REGIST_ID", updatable = false)
    private String creatorId;

    @Column(name = "UPDT_DTTM")
    private Date updateAt;

    @JsonIgnore
    @Column(name = "UPDT_ID")
    private String updaterId;

    @PrePersist
    public void prePersistAction() {
        final Date date = new Date();
        if (!StringUtils.hasText(this.creatorId)) this.creatorId = SessionUtils.getUsername();
        this.createAt = date;
        if (!StringUtils.hasText(this.updaterId)) this.updaterId = SessionUtils.getUsername();
        this.updateAt = date;
    }

    @PreUpdate
    public void preUpdateAction() {
        if (!StringUtils.hasText(this.updaterId)) this.updaterId = SessionUtils.getUsername();
        this.updateAt = new Date();
    }

    public String getCreator() {
        return SessionUtils.getUsername(creatorId);
    }

    public String getUpdater() {
        return SessionUtils.getUsername(updaterId);
    }
}
