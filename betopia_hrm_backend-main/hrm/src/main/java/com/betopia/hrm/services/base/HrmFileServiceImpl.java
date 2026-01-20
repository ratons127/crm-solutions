package com.betopia.hrm.services.base;

// iText PDF imports
// iText PDF imports
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

// Apache POI Excel imports

// Apache Commons CSV imports
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

// Spring imports
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// Java standard imports
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Date;

// Your custom imports
import com.betopia.hrm.domain.base.annotation.ExposeField;

@Service
public class HrmFileServiceImpl<T> implements HrmFileService<T> {

    // Get only fields with @ExposeField annotation
    private Field[] getExposedFields() {
        return Arrays.stream(getEntityClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExposeField.class))
                .toArray(Field[]::new);
    }

    @Override
    public List<T> importFromExcel(MultipartFile file) throws Exception {
        List<T> entities = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (!rows.hasNext()) return entities;

            // Header row
            Row headerRow = rows.next();
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    headerMap.put(cell.getStringCellValue().trim(), i);
                }
            }

            // Data rows
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                T entity = createEntityFromRow(currentRow, headerMap);
                if (entity != null) {
                    entities.add(entity);
                }
            }
        }

        return entities;
    }

    @Override
    public ByteArrayOutputStream exportToExcel(List<T> data) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            Field[] fields = getExposedFields();

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            ((org.apache.poi.ss.usermodel.Font) font).setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont((org.apache.poi.ss.usermodel.Font) font);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);

            // Header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < fields.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(getFieldDisplayName(fields[i]));
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);

            int rowNum = 1;
            for (T entity : data) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(entity);
                    Cell cell = row.createCell(i);
                    setCellValue(cell, value, fields[i]); // ✅ Fixed - 3 parameters
                    cell.setCellStyle(dataStyle);
                }
            }

            // Auto-size columns
            for (int i = 0; i < fields.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }

            workbook.write(outputStream);
        }

        return outputStream;
    }

    @Override
    public ByteArrayOutputStream exportToCsv(List<T> data) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PrintWriter writer = new PrintWriter(outputStream);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader())) {

            Field[] fields = getExposedFields();

            // Header
            List<String> headers = Arrays.stream(fields)
                    .map(this::getFieldDisplayName)
                    .collect(Collectors.toList());
            csvPrinter.printRecord(headers);

            // Data
            for (T entity : data) {
                List<Object> values = new ArrayList<>();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    values.add(formatFieldValue(value, field));
                }
                csvPrinter.printRecord(values);
            }

            csvPrinter.flush();
        }

        return outputStream;
    }

    @Override
    public ByteArrayOutputStream exportToPdf(List<T> data) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph(getEntityClass().getSimpleName() + " Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Table
            Field[] fields = getExposedFields();
            PdfPTable table = new PdfPTable(fields.length);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            // Headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            for (Field field : fields) {
                PdfPCell cell = new PdfPCell(new Phrase(getFieldDisplayName(field), headerFont));
                cell.setBackgroundColor(new BaseColor(52, 73, 94));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Data
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            int rowCount = 0;
            for (T entity : data) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    PdfPCell cell = new PdfPCell(new Phrase(formatFieldValue(value, field), dataFont));
                    cell.setPadding(5);

                    // Alternate row colors
                    if (rowCount % 2 == 0) {
                        cell.setBackgroundColor(new BaseColor(236, 240, 241));
                    }
                    table.addCell(cell);
                }
                rowCount++;
            }

            document.add(table);

            // Footer
            Paragraph footer = new Paragraph("\nGenerated on: " + new Date(),
                    FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY));
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

        } finally {
            document.close();
        }

        return outputStream;
    }

    @Override
    public Class<T> getEntityClass() {
        return null;
    }

    private T createEntityFromRow(Row row, Map<String, Integer> headerMap) throws Exception {
        T entity = getEntityClass().getDeclaredConstructor().newInstance();
        Field[] fields = getExposedFields();

        for (Field field : fields) {
            String fieldName = getFieldDisplayName(field);
            Integer columnIndex = headerMap.get(fieldName);

            if (columnIndex != null) {
                Cell cell = row.getCell(columnIndex);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    field.setAccessible(true);
                    Object value = getCellValue(cell, field);
                    if (value != null) {
                        field.set(entity, value);
                    }
                }
            }
        }

        return entity;
    }

    private Object getCellValue(Cell cell, Field field) {
        if (cell == null) return null;

        Class<?> fieldType = field.getType();

        // Skip relationships (Collections, other entities)
        if (Collection.class.isAssignableFrom(fieldType)) {
            return null; // Collections handled separately
        }

        try {
            switch (cell.getCellType()) {
                case STRING:
                    String strValue = cell.getStringCellValue().trim();
                    if (strValue.isEmpty()) return null;

                    if (fieldType == String.class) return strValue;
                    if (fieldType == Long.class || fieldType == long.class)
                        return Long.parseLong(strValue);
                    if (fieldType == Integer.class || fieldType == int.class)
                        return Integer.parseInt(strValue);
                    if (fieldType == Double.class || fieldType == double.class)
                        return Double.parseDouble(strValue);
                    if (fieldType == Boolean.class || fieldType == boolean.class)
                        return Boolean.parseBoolean(strValue);
                    return strValue;

                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue();
                    }
                    double numValue = cell.getNumericCellValue();
                    if (fieldType == Integer.class || fieldType == int.class)
                        return (int) numValue;
                    if (fieldType == Long.class || fieldType == long.class)
                        return (long) numValue;
                    return numValue;

                case BOOLEAN:
                    return cell.getBooleanCellValue();

                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private void setCellValue(Cell cell, Object value, Field field) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }

        // Handle Collections (like subCategories)
        if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) value;
            cell.setCellValue(collection.size() + " items");
            return;
        }

        // Handle entity relationships
        if (!isPrimitiveOrWrapper(value.getClass()) && !value.getClass().equals(String.class)) {
            // For relationships like parent category
            try {
                Field idField = value.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                Object idValue = idField.get(value);

                Field nameField = value.getClass().getDeclaredField("name");
                nameField.setAccessible(true);
                Object nameValue = nameField.get(value);

                cell.setCellValue(nameValue != null ? nameValue.toString() : "ID: " + idValue);
            } catch (Exception e) {
                cell.setCellValue(value.toString());
            }
            return;
        }

        // Primitive types
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private String formatFieldValue(Object value, Field field) {
        if (value == null) return "";

        if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) value;
            return collection.size() + " items";
        }

        if (!isPrimitiveOrWrapper(value.getClass()) && !value.getClass().equals(String.class)) {
            try {
                Field nameField = value.getClass().getDeclaredField("name");
                nameField.setAccessible(true);
                Object nameValue = nameField.get(value);
                return nameValue != null ? nameValue.toString() : value.toString();
            } catch (Exception e) {
                return value.toString();
            }
        }

        return value.toString();
    }

    private String getFieldDisplayName(Field field) {
        // Use field name, you can customize this
        String name = field.getName();
        // Convert camelCase to Title Case
        return name.substring(0, 1).toUpperCase() +
                name.substring(1).replaceAll("([A-Z])", " $1").trim();
    }

    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
                type.equals(String.class) ||
                type.equals(Integer.class) ||
                type.equals(Long.class) ||
                type.equals(Double.class) ||
                type.equals(Float.class) ||
                type.equals(Boolean.class) ||
                type.equals(Byte.class) ||
                type.equals(Short.class);
    }
}
