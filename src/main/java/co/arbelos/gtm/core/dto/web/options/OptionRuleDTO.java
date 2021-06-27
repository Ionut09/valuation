package co.arbelos.gtm.core.dto.web.options;

import co.arbelos.gtm.core.dao.jato.ExtraOptionRuleProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OptionRuleDTO {
    private String condition;
    private String rule;

    public OptionRuleDTO(ExtraOptionRuleProjection extraOptionRuleProjection) {
        this.condition = extraOptionRuleProjection.getCondition();
        this.rule = extraOptionRuleProjection.getRule();
    }
}
