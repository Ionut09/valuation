package co.arbelos.gtm.core.domain.vin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "vin_query", indexes = { @Index(name = "vin_query_index", columnList = "vin") })
@Data
@NoArgsConstructor
@ToString
public class VinQuery {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vin")
    private String vin;

    @Lob
    @Column(name = "data")
    private String data;

    public VinQuery(String vin, String data) {
        this.vin = vin;
        this.data = data;
    }
}
