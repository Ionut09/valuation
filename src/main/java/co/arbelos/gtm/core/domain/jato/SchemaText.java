package co.arbelos.gtm.core.domain.jato;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "schema_text")
@Data
@NoArgsConstructor
public class SchemaText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * - The language_id field contains the language code (see Appendix C: Language Database Matrix)
     * - The schema_id field contains the schema ID of the item or the attribute
     * - The data_value contains the actual data of the specific feature; this field can contain any character including UTF­8.
     * To determine what data it contains the data_value type needs to be looked at.
     * - The abbr_text contains the short translation, always maximum 15 characters.
     * - The full_text contains the long translation, always maximum 40 characters.
     */

    @Column(name = "language_id")
    private Integer languageId;

    @Column(name = "schema_id")
    private Integer schemaId;

    @Column(name = "data_value")
    private String dataValue;

    @Column(name = "abbr_text")
    private String shortTranslation;

    @Column(name = "full_text")
    private String longTranslation;
}
