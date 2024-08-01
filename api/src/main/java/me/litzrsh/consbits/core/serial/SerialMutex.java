package me.litzrsh.consbits.core.serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.litzrsh.consbits.core.CommonConstants;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "SERIAL_MUTEX")
@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class SerialMutex implements Serializable {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    @Id
    @Column(name = "SERIAL_ID")
    private String id;
}
