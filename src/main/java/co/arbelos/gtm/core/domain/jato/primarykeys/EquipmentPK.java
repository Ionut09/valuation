package co.arbelos.gtm.core.domain.jato.primarykeys;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EquipmentPK implements Serializable {

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "schema_id")
    private Integer schemaId;

    @Column(name = "option_id")
    private Integer optionId;

    @Column(name = "record_id")
    private Integer recordId;
}
