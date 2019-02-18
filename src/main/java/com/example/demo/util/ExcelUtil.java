package com.example.demo.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class ExcelUtil<T> {
    /**
     * 导出Excel的方法
     *
     * @param title   excel中的sheet名称
     * @param headers 表头
     * @param result  结果集
     * @param out     输出流
     * @param pattern 时间格式
     * @throws Exception
     */
    public void exportExcel(String title, String[] headers, String[] columns, Collection<T> result, OutputStream out, String pattern) throws Exception {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.GOLD.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        // font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 产生表格标题行
        // 表头的样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();// 创建样式对象
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        // 设置字体
        HSSFFont titleFont = workbook.createFont(); // 创建字体对象
        titleFont.setFontHeightInPoints((short) 15); // 设置字体大小
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
        // titleFont.setFontName("黑体"); // 设置为黑体字
        titleStyle.setFont(titleFont);
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (headers.length - 1)));// 指定合并区域
        HSSFRow rowHeader = sheet.createRow(0);
        HSSFCell cellHeader = rowHeader.createCell((short) 0); // 只能往第一格子写数据，然后应用样式，就可以水平垂直居中
        HSSFRichTextString textHeader = new HSSFRichTextString(title);
        cellHeader.setCellStyle(titleStyle);
        cellHeader.setCellValue(textHeader);

        HSSFRow row = sheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        // 遍历集合数据，产生数据行
        if (result != null) {
            int index = 2;
            for (T t : result) {
                // Field[] fields = t.getClass().getDeclaredFields();
                row = sheet.createRow(index);
                index++;
                for (short i = 0; i < columns.length; i++) {
                    HSSFCell cell = row.createCell(i);
                    // Field field = fields[i];
                    // String fieldName = field.getName();
                    String fieldName = columns[i];
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Class tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    // getMethod.getReturnType().isInstance(obj);
                    Object value = getMethod.invoke(t, new Class[]{});
                    String textValue = null;
                    if (value == null) {
                        textValue = "";
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else if (value instanceof byte[]) {
                        // 有图片时，设置行高为60px;
                        row.setHeightInPoints(60);
                        // 设置图片所在列宽度为80px,注意这里单位的一个换算
                        sheet.setColumnWidth(i, (short) (35.7 * 80));
                        // sheet.autoSizeColumn(i);
                        byte[] bsValue = (byte[]) value;
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
                        anchor.setAnchorType(2);
                        patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                    }

                    if (textValue != null) {
                        HSSFRichTextString richString = new HSSFRichTextString(textValue);
                        cell.setCellValue(richString);
                    }
                }
            }
        }
        workbook.write(out);
    }

    public void exportExcelBasic(String title, String[] headers, String[] columns, T result, OutputStream out, String pattern) throws Exception {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.GOLD.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        // font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 产生表格标题行
        // 表头的样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();// 创建样式对象
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        // 设置字体
        HSSFFont titleFont = workbook.createFont(); // 创建字体对象
        titleFont.setFontHeightInPoints((short) 15); // 设置字体大小
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
        // titleFont.setFontName("黑体"); // 设置为黑体字
        titleStyle.setFont(titleFont);
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (headers.length - 1)));// 指定合并区域
        HSSFRow rowHeader = sheet.createRow(0);
        HSSFCell cellHeader = rowHeader.createCell((short) 0); // 只能往第一格子写数据，然后应用样式，就可以水平垂直居中
        HSSFRichTextString textHeader = new HSSFRichTextString(title);
        cellHeader.setCellStyle(titleStyle);
        cellHeader.setCellValue(textHeader);
        for (int idx = 0; idx < headers.length; idx++) {
            HSSFRow row = sheet.createRow(idx + 1);
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[idx]);
            cell1.setCellValue(text);
            HSSFCell cell = row.createCell(1);
            // Field field = fields[i];
            // String fieldName = field.getName();
            String fieldName = columns[idx];
            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Class tCls = result.getClass();
            Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
            // getMethod.getReturnType().isInstance(obj);
            Object value = getMethod.invoke(result, new Class[]{});
            String textValue = null;
            if (value == null) {
                textValue = "";
            } else if (value instanceof Date) {
                Date date = (Date) value;
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                textValue = sdf.format(date);
            } else if (value instanceof byte[]) {
                // 有图片时，设置行高为60px;
                row.setHeightInPoints(60);
                // 设置图片所在列宽度为80px,注意这里单位的一个换算
                sheet.setColumnWidth(idx, (short) (35.7 * 80));
                // sheet.autoSizeColumn(i);
                byte[] bsValue = (byte[]) value;
                HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, idx + 1, (short) 6, idx + 1);
                anchor.setAnchorType(2);
                patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
            } else {
                // 其它数据类型都当作字符串简单处理
                textValue = value.toString();
            }

            if (textValue != null) {
                HSSFRichTextString richString = new HSSFRichTextString(textValue);
                cell.setCellValue(richString);
            }
        }
        workbook.write(out);
    }

    public static String toUtf8String(String fileName, HttpServletRequest request) throws Exception {
        final String userAgent = request.getHeader("USER-AGENT");
        String finalFileName = null;
        if (StringUtils.contains(userAgent, "MSIE")) {// IE浏览器
            finalFileName = URLEncoder.encode(fileName, "UTF8");
        } else if (StringUtils.contains(userAgent, "Mozilla")) {// google,火狐浏览器
            finalFileName = new String(fileName.getBytes(), "ISO8859-1");
        } else {
            finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
        }
        return finalFileName;
    }

    /**
     * 解析Excel内容（仅支持Excel2007之前版本，即扩展名为xls的版本）
     *
     * @param filePath    Excel文件地址
     * @param rangeRow    数据范围行数（A:从1开始；B:包含列头；C:>0时为已知数据行数，<=0时为未知行数范围即不定行数，此时当某行第一列为空时，结束读取 ）
     * @param rangeCol    数据范围列数（A:从1开始；B:必须指定范围）
     * @param titleHeight 标题行数
     * @param keyRowNo    数据KEY所在行号（A:<=0时为无数据KEY，此时数据结果，将以行索引号为KEY）
     * @return 解析后Excel内容
     */
//    public static List<Map<String, String>> parseExcel(String filePath, int rangeRow, int rangeCol, int titleHeight, int keyRowNo) {
//        XSSFWorkbook wb = null;
//        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
//        try {
//            InputStream is = new FileInputStream(filePath);
//            wb = new XSSFWorkbook(is);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        XSSFSheet sheet = wb.getSheetAt(0);
//
//        // 获取处理数据结构KEY
//        String[] keyRow = null;
//        XSSFRow keyRowObj = null;
//        if (keyRowNo > 0) {
//            keyRow = new String[rangeCol - 1];
//            keyRowObj = sheet.getRow(keyRowNo - 1);
//        }
//        for (int i = 0; i < rangeCol - 1; i++) {
//            keyRow[i] = getCellFormatValue(keyRowObj.getCell(i));
//        }
//
//        Map<String, String> dataMap = null;
//        XSSFRow dataRowObj = null;
//        String mapKey = "";
//        // 获取数据(已知数据行数)
//        if (rangeRow > 0) {
//            for (int i = titleHeight; i < rangeRow; i++) {
//                dataRowObj = sheet.getRow(i);
//                dataMap = new HashMap<>();
//                for (int j = 0; j < rangeCol; j++) {
//                    if (keyRow != null) {
//                        mapKey = keyRow[j];
//                    } else {
//                        mapKey = String.valueOf(j);
//                    }
//                    dataMap.put(mapKey, getCellFormatValue(dataRowObj.getCell(j)));
//                }
//                result.add(dataMap);
//            }
//        }
//        // 未知数据行数
//        else {
//            int rowIndex = titleHeight;
//            while (true) {
//                dataRowObj = sheet.getRow(rowIndex);
//                if (dataRowObj == null || StringUtils.isBlank(getCellFormatValue(dataRowObj.getCell(0)))) {
//                    break;
//                }
//                dataMap = new HashMap<>();
//                for (int j = 0; j < rangeCol - 1; j++) {
//                    if (keyRow != null) {
//                        mapKey = keyRow[j];
//                    } else {
//                        mapKey = String.valueOf(j);
//                    }
//                    dataMap.put(mapKey, getCellFormatValue(dataRowObj.getCell(j)));
//                }
//                result.add(dataMap);
//                rowIndex++;
//            }
//        }
//
//        return result;
//    }

    /**
     * 根据HSSFCell类型设置数据
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(XSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case XSSFCell.CELL_TYPE_NUMERIC:
                case XSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式

                        // 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();

                        // 方法2：这样子的data格式是不带带时分秒的：2011-10-12
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);

                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case XSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }


    /**
     * 填充Excel模板文件内容
     *
     * @param result    文档数据
     * @param fileName  Excel模板文件名称
     * @param date      当前日期
     * @param creator   当前操作员
     * @param startRow  填充起始行
     * @param startCell 填充起始列
     */
    public static void fillData(List<List<String>> result, String fileName, String date, String creator, int startRow, int startCell, OutputStream out) {
        try {
            InputStream is = ExcelUtil.class.getClassLoader().getResourceAsStream(fileName);
            XSSFWorkbook wb = new XSSFWorkbook(is);
            XSSFSheet sheet = wb.getSheetAt(0);
            int nameCount = wb.getNumberOfNames();
            for (int i = 0; i < nameCount; i++) {
                XSSFName name = wb.getNameAt(i);
                if ("CreateDate".equals(name.getNameName())) {
                    System.out.println();
                }
                if ("Creator".equals(name.getNameName())) {

                }
            }
            for (List<String> datas : result) {
                int firstCell = startCell;
                //获取单元格
                XSSFRow row = sheet.createRow(startRow);
                for (String data : datas) {
                    XSSFCell cell = row.createCell(firstCell);
                    //写入单元格内容
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(data);
                    firstCell++;
                }
                startRow++;
            }
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 填充Excel模板文件内容,多个sheet
     *
     * @param result    文档数据
     * @param fileName  Excel模板文件名称
     * @param date      当前日期
     * @param creator   当前操作员
     * @param startRow  填充起始行
     * @param startCell 填充起始列
     */
    public static void fillDataAll(List<List<List<String>>> result, String fileName, String date, String creator, int[] startRow, int startCell, OutputStream out) {
        try {
            InputStream is = ExcelUtil.class.getClassLoader().getResourceAsStream(fileName);
            XSSFWorkbook wb = new XSSFWorkbook(is);
            for (int i = 0; i < result.size(); i++) {
                XSSFSheet sheet = wb.getSheetAt(i);
                int start = startRow[i];
                for (List<String> datas : result.get(i)) {
                    int firstCell = startCell;
                    //获取单元格
                    XSSFRow row = sheet.createRow(start);
                    for (String data : datas) {
                        XSSFCell cell = row.createCell(firstCell);
                        //写入单元格内容
                        cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(data);
                        firstCell++;
                    }
                    start++;
                }
            }
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
