package me.litzrsh.consbits.core;

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
public class SearchParams implements Serializable {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private int page = 1;
    private int perPage = 10;

    public int getOffset() {
        return (page - 1) * perPage;
    }
}
