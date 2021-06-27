package co.arbelos.gtm.core.domain.jato;

import co.arbelos.gtm.core.domain.jato.primarykeys.OptionTextPK;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "option_text")
@Data
@NoArgsConstructor
public class OptionText {

    @EmbeddedId
    private OptionTextPK optionTextPK;

    @Column(name = "content")
    private String content;
}
