package co.arbelos.gtm.core.dto.web.valuation;


import co.arbelos.gtm.core.domain.valuation.CarValuation;
import co.arbelos.gtm.core.dto.web.wizard.VehicleDTO;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValuationHistoryDTO {
    private CarValuation valuation;
    private VehicleDTO vehicleDTO;
    private String imageUrl;

    public ValuationHistoryDTO(CarValuation valuation, VehicleDTO vehicleDTO) {
        this.valuation = valuation;
        this.vehicleDTO = vehicleDTO;
    }
}
