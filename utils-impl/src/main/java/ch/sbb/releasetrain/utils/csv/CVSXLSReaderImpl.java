package ch.sbb.releasetrain.utils.csv;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import ch.sbb.releasetrain.utils.config.GlobalConfig;
import ch.sbb.releasetrain.utils.http.HttpUtil;

import com.google.inject.Inject;

/**
 * Liest die, zu einem Produkt Bsp: Angebot relevanten informationen aus dem products.xlsx
 * ung gibt diese als Map / Liste zurueck
 *
 */
@CommonsLog
public class CVSXLSReaderImpl implements CVSXLSReader {

    @Inject
    private HttpUtil http;

    @Inject
    private GlobalConfig config;

    @Setter
    private String text;

    @Setter
    private String fileUrlKey = "product.config.url";

    @Override
    public List<String> getListFromColoumn(String spalte) {
        return new ArrayList<>(getMapFromXLS(spalte, spalte).values());
    }

    @Override
    public Map<String, String> getMapFromXLS(String coloumn1, String coloumn2) {

        Map<String, String> ret = new HashMap<>();
        try {

            if (text == null) {
                text = http.getPageAsString(config.get(fileUrlKey));
            }

            final CSVParser parser = new CSVParser(new StringReader(text), CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader());

            for (CSVRecord record : parser.getRecords()) {
                String c1 = record.get(coloumn1);
                String c2 = record.get(coloumn2);
                ret.put(c1, c2);
            }

        } catch (Exception e) {
            log.error(e);
        }
        return ret;
    }

}
