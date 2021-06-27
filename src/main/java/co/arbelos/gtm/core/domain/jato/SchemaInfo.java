package co.arbelos.gtm.core.domain.jato;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "schema_info")
@Data
@NoArgsConstructor
public class SchemaInfo {

    @Id
    @Column(name = "schema_id", nullable = false)
    private Integer schemaId;

    @Column(name = "parent_schema_id")
    private Integer parentSchemaId;

    @Column(name = "location_schema_id")
    private Integer locationSchemaId;

    @Column(name = "scale_of_data")
    private Integer scaleOfData;

    @Column(name = "data_type")
    private Integer dataType;
}
