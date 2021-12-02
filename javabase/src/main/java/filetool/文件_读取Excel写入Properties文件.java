package filetool;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName ReadExcelToProperties
 * @create 2020-06-18 8:49
 * @Des TODO
 */
public class 文件_读取Excel写入Properties文件 {
    public static void main(String[] args) throws IOException {
        Workbook wb = null;   //读取excel文件会返回wd对象
        Sheet sheet = null;   //读取表中第一个sheet对象返回值
        Row row = null;       //某一行数据row
//        List<Map<String,String>> list = null;
        String cellData = null;  //某单元格读取出来的数值
        String filePath = "C:\\tmp\\opc.xlsx";
        String fileOutPath = "C:\\tmp\\opchive.txt";
        FileWriter fileWriter = new FileWriter(fileOutPath);
//        String columns[] = {"name","age","score"};
        wb = readExcel(filePath);
        if (wb != null) {
            //用来存放表中数据
//            list = new ArrayList<Map<String, String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            System.out.println("总行数：" + rownum);
            //获取第一行
            row = sheet.getRow(0);
            //获取最大行数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i < rownum; i++) {
//                Map<String, String> map = new LinkedHashMap<String, String>();
                Row newRow = sheet.getRow(i);
                if (newRow != null) {
                    String head = (String) getCellFormatValue(newRow.getCell(0)) + "^";
                    for (int j = 1; j < colnum; j++) {
                        cellData = ((String) getCellFormatValue(newRow.getCell(j))).replaceAll("\n", "。").replaceAll(" ","");
                        if ("".equals(cellData)) {
                            cellData = "empty";
                        }
                        head = head + cellData;
                        if (j != colnum - 1) {
                            head = head + "^";
                        }
//                        map.put(columns[j], cellData);
                    }
                    fileWriter.write(head + "\r\n");
                } else {
                    break;
                }
//                list.add(map);
            }
        }
        /*//遍历解析出来的list
        for (Map<String, String> map : list) {
            for (Entry<String, String> entry : map.entrySet()) {
                System.out.print(entry.getKey() + ":" + entry.getValue() + ",");
            }
            System.out.println();
        }*/
    }

    //读取封装excel对象,return：.xls -> new HSSFWorkbook  .xlsx -> new XSSFWorkbook
    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    //单个单元格读取值return：Object对象
    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString().replaceAll("\r\n","。");
                    break;
                }
                case Cell.CELL_TYPE_BOOLEAN: {
                    cellValue = ((Object) cell.getBooleanCellValue()).toString().toUpperCase();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }
}