package co.arbelos.gtm.core.dto.web.wizard;

import co.arbelos.gtm.core.dao.wizard.TransmissionDescriptionProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransmissionDescriptionDTO {
    private String text;
    private Integer languageId;

    public TransmissionDescriptionDTO(TransmissionDescriptionProjection transmissionDescriptionProjection) {
        this.text = transmissionDescriptionProjection.getText();
        this.languageId = transmissionDescriptionProjection.getLanguageId();
    }
}
