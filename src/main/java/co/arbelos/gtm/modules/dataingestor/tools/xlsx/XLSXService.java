package co.arbelos.gtm.modules.dataingestor.tools.xlsx;

import co.arbelos.gtm.core.domain.registrations.CarRegistration;
import co.arbelos.gtm.core.repository.registrations.CarRegistrationRepository;
import co.arbelos.gtm.core.util.JatoUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class XLSXService {

    private static final Logger log = LoggerFactory.getLogger(XLSXService.class);

    private final CarRegistrationRepository carRegistrationRepository;

    public XLSXService(CarRegistrationRepository carRegistrationRepository) {
        this.carRegistrationRepository = carRegistrationRepository;
    }

    public void handleFile(MultipartFile multipartFile) {
        log.info("Trying to parse XLSX file: {}", multipartFile.getOriginalFilename());

        parseFile(multipartFile);
    }

    private void parseFile(MultipartFile multipartFile) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);

            log.debug("Parsing {} XLSX rows!", sheet.getPhysicalNumberOfRows());

            for (Row row : sheet) {
                List<CarRegistration> cars = new ArrayList<>();
                // skip header
                if (row.getCell(0).getStringCellValue().equals("Brand")) {
                    continue;
                }

                // skip last line
                if (row.getCell(0).getStringCellValue().equals("")) {
                    continue;
                }

                CarRegistration carRegistration = new CarRegistration();

                if (row.getCell(0).getCellType() == CellType.STRING) {
                    carRegistration.setManufacturer(row.getCell(0).getStringCellValue());
                }

                if (row.getCell(1).getCellType() == CellType.STRING) {
                    carRegistration.setModel(row.getCell(1).getStringCellValue().split(":")[1]);
                }

                if (row.getCell(2).getCellType() == CellType.NUMERIC) {
                    carRegistration.setEngineCC((int) row.getCell(2).getNumericCellValue());
                }

                if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                    carRegistration.setEngineLitres((float)row.getCell(3).getNumericCellValue());
                }

                if (row.getCell(4).getCellType() == CellType.STRING) {
                    carRegistration.setFuel(JatoUtil.getFuelTypeCode(capitalize(row.getCell(4).getStringCellValue())));
                }

                if (row.getCell(6).getCellType() == CellType.STRING) {
                    carRegistration.setBody(JatoUtil.getBodyTypeCode(capitalize(row.getCell(6).getStringCellValue())));
                }

                if (row.getCell(8).getCellType() == CellType.NUMERIC) {
                    carRegistration.setEngineHP((int)row.getCell(8).getNumericCellValue());
                }

                // iterate on cells for the current row
                Iterator<Cell> cellIterator = row.cellIterator();
                int index = 0;
                int threshold = 9;

                LocalDate startDate = LocalDate.parse("2014-03-01");

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (index >= threshold) {
                        CarRegistration _car = CarRegistration.from(carRegistration, startDate, (int)cell.getNumericCellValue());
                        cars.add(_car);
                        startDate = startDate.plusMonths(1L);
                    }

                    index++;
                }

                carRegistrationRepository.saveAll(cars);
            }

            workbook.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
