package co.arbelos.gtm.core.domain.marketobservation;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "market_observation")
@Data
@NoArgsConstructor
@ToString
public class MarketObservation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jato_vehicle_id")
    private Long vehicleId;

    @Column(name = "market_data_uuid")
    @Type(type="uuid-char")
    private UUID marketId;

}
