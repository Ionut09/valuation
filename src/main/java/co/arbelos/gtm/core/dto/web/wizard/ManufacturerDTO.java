package co.arbelos.gtm.core.dto.web.wizard;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class ManufacturerDTO implements Serializable {
    private String name;

    public ManufacturerDTO(String name) {
        this.name = name;
    }
}
