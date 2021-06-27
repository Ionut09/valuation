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
public class OptionTextPK implements Serializable {

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "language_id")
    private Integer languageId;

    @Column(name = "option_id")
    private Integer optionId;

    @Column(name = "order_id")
    private Integer orderId;
}
