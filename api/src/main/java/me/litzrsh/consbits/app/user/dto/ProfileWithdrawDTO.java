package me.litzrsh.consbits.app.user.dto;

import me.litzrsh.consbits.core.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProfileWithdrawDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private String reasonCode;
    private String reason;
}
