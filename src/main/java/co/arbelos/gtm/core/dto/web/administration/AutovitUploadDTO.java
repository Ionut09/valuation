package co.arbelos.gtm.core.dto.web.administration;

import co.arbelos.gtm.core.domain.marketobservation.MarketData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AutovitUploadDTO implements Serializable {
    private List<MarketData> ads;
    private List<String> errors;

    @JsonIgnore
    private Long ingestHistoryId;
}
