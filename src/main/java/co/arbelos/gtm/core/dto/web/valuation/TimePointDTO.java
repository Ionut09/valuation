package co.arbelos.gtm.core.dto.web.valuation;

import co.arbelos.gtm.core.dao.valuation.TimePointProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimePointDTO {
    private LocalDate date;
    private Float value;

    public TimePointDTO(TimePointProjection timePointProjection) {
        this.date = timePointProjection.getDate();
        this.value = timePointProjection.getValue();
    }
}
