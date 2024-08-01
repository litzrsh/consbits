package me.litzrsh.consbits.app.user;

import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.SearchParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchParams extends SearchParams {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private String type;
    private String status;
    private String query;
}
