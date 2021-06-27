package co.arbelos.gtm.core.domain.jato;

import co.arbelos.gtm.core.domain.jato.primarykeys.EquipmentPK;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "equipment")
@Data
@NoArgsConstructor
public class Equipment {

    @EmbeddedId
    private EquipmentPK equipmentPK;

    @Column(name = "location")
    private String location;

    @Column(name = "data_value")
    private String data;

    @Lob
    @Column(name = "`condition`")
    private String condition;
}
