package co.arbelos.gtm.core.dto.web.options;


import co.arbelos.gtm.core.dao.options.StandardOptionProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StandardOptionDTO {
    private Long schemaId;
    private Long languageId;

    private String content;

    public StandardOptionDTO(StandardOptionProjection standardOptionProjection) {
        this.schemaId = standardOptionProjection.getSchemaId();
        this.languageId = standardOptionProjection.getLanguageId();
        this.content = standardOptionProjection.getContent();
    }
}
