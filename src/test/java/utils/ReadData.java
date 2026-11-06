package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads key/value test data from test_data.xlsx (column A = key, column B = value).
 * Rows are looked up by their label, so the order of rows in the sheet does not matter.
 */
public class ReadData {

    static String testDataPath = System.getProperty("user.dir") + "/src/test/java/testData/test_data.xlsx";

    private static final Map<String, String> data = new HashMap<>();

    static {
        try (FileInputStream fileInputStream = new FileInputStream(testDataPath);
             XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

            XSSFSheet sheet = workbook.getSheetAt(0);   // first sheet, whatever it is called
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                Cell keyCell = row.getCell(0);
                Cell valueCell = row.getCell(1);
                if (keyCell == null || valueCell == null) {
                    continue;
                }
                String key = formatter.formatCellValue(keyCell).trim().toLowerCase();
                if (!key.isEmpty()) {
                    data.put(key, formatter.formatCellValue(valueCell).trim());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read test data from " + testDataPath, e);
        }
    }

    public static String get(String key) {
        String value = data.get(key.toLowerCase());
        if (value == null) {
            throw new IllegalArgumentException("No test data found for key '" + key + "' in " + testDataPath);
        }
        return value;
    }

    public static String username = get("login email");
    public static String password = get("login password");

    public ReadData() throws Exception {
    }

}
