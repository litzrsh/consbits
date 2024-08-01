package me.litzrsh.consbits.core.serial;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.litzrsh.consbits.core.CommonConstants;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SERIAL_BASE")
@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class Serial implements Serializable {

    @java.io.Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    @Id
    @Column(name = "SERIAL_ID")
    private String id;

    @Column(name = "SERIAL_VAL")
    private Long value;

    @Column(name = "UPDT_DTTM")
    private Date updateAt;

    public Serial(String id, Date date) {
        this.id = id;
        this.value = 1L;
        this.updateAt = date;
    }

    @PrePersist
    public void prePersistAction() {
        if (updateAt == null) updateAt = new Date();
    }
}
