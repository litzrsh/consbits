package me.litzrsh.consbits.app.user.dto;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.core.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private String type;
    private String loginId;
    private String password;
    private String name;
    private String remarks;
    private String imageUrl;
    private String email;
    private String mobile;
    private String status;
    private List<Authority> authorities = new ArrayList<>();
}
