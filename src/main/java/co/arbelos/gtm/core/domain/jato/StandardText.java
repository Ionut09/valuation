package co.arbelos.gtm.core.domain.jato;


import co.arbelos.gtm.core.domain.jato.primarykeys.StandardTextPK;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "standard_text")
@Data
@NoArgsConstructor
public class StandardText {

    /**
     * vehicle_id The reference number of the vehicle
     * schema_id The schema id for the item
     * language_id The language code (see Appendix C: Language Database Matrix). content The descriptive text.
     */
    @EmbeddedId
    private StandardTextPK standardTextPK;

    @Column(name = "content")
    private String content;
}
