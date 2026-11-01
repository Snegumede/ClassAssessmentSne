package utils;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class ReadData {

    static String testDataPath = System.getProperty("user.dir") + "/src/test/java/testData/test_data.xlsx";
    static FileInputStream fileInputStream;

    static {
        try {
            fileInputStream = new FileInputStream(testDataPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static XSSFWorkbook workbook;

    static {
        try {
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ReadData() throws Exception {
    }

    static XSSFSheet sheet  = workbook.getSheet( "Sheet 1");

    public static String username = sheet.getRow(1).getCell(1).getStringCellValue();
    public static String password = sheet.getRow(2).getCell(1).getStringCellValue();

    //row indexes increment with 1 to cater for (Numbers) formatting vs MS Excel - For reference
//    public static String username = sheet.getRow(2).getCell(0).getStringCellValue();
//    public static String password = sheet.getRow(2).getCell(1).getStringCellValue();

//
//    @Test
//    public void testData() {
//        System.out.println("Username: " + username);
//        System.out.println("Password: " + password);
//    }

}
