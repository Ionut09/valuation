package co.arbelos.gtm.core.domain.jato;

import co.arbelos.gtm.core.domain.jato.primarykeys.OptionNamePK;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "option_name")
@Data
@NoArgsConstructor
public class OptionName {

    @EmbeddedId
    private OptionNamePK optionNamePK;

    @Column(name = "manuf_name")
    private String manufacturerName;
}
