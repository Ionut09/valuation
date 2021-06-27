package co.arbelos.gtm.core.dao.valuation;

import java.time.LocalDate;

public interface TimePointProjection {
    LocalDate getDate();
    Float getValue();
}
