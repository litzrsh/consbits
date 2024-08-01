package me.litzrsh.consbits.app.user;

import me.litzrsh.consbits.core.BaseModel;
import me.litzrsh.consbits.core.CommonConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Entity
@Table(name = "USER_IMAGE")
@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class UserImage extends BaseModel {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    @Id
    @Column(name = "USER_ID")
    private String id;

    @Column(name = "FILE_NM")
    private String name;

    @Column(name = "FILE_SZ")
    private Long size;

    @Column(name = "FILE_TYPE")
    private String type;

    @Column(name = "FILE_PATH")
    private String path;

    @Column(name = "FILE_URL")
    private String url;
}
