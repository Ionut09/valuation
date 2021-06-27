package co.arbelos.gtm.core.domain.vin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "vin_query_mapper", indexes = { @Index(name = "vin_query_index", columnList = "gtws_make,gtws_model") })
@Data
@NoArgsConstructor
@ToString
public class VinQueryMapper {

    @Id
    private String id;

    @Column(name = "gtws_make")
    private String gtwsMake;

    @Column(name = "gtws_model")
    private String gtwsModel;

    @Column(name = "jato_make")
    private String jatoMake;

    @Column(name = "jato_model")
    private String jatoModel;
}
