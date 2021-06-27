package co.arbelos.gtm.core.domain.marketobservation;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "market_data_ingest_history")
@Data
@NoArgsConstructor
public class MarketDataIngestHistory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ingestion_date")
    private LocalDate date;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Lob
    @Column(name = "details")
    private String details;

    public MarketDataIngestHistory(String details, String fileName, Long fileSize) {
        this.date = LocalDate.now();

        this.details = details;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
}
