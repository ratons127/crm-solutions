package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.dto.employee.EmployeeUploadDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ExcelHelper {

    private static final Logger log = LoggerFactory.getLogger(ExcelHelper.class);

    public List<EmployeeUploadDTO> excelToEmployeeDTOs(InputStream is) {
        try (Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<EmployeeUploadDTO> list = new ArrayList<>();
            Iterator<Row> rows = sheet.iterator();
            boolean header = true;

            while (rows.hasNext()) {
                Row row = rows.next();
                if (header) {
                    header = false;
                    continue;
                }

                if (isRowEmpty(row)) {
                    log.info("Skipping empty row: " + (row.getRowNum() + 1));
                    continue;
                }

                try {
                    EmployeeUploadDTO dto = new EmployeeUploadDTO();
                    dto.setEmployeeId(getLongValue(row.getCell(0)));
                    dto.setCompany(row.getCell(1).getStringCellValue());
                    dto.setBusinessUnit(row.getCell(2).getStringCellValue());
                    dto.setWorkplaceGroup(row.getCell(3).getStringCellValue());
                    dto.setWorkplace(row.getCell(4).getStringCellValue());
                    dto.setDepartment(row.getCell(5).getStringCellValue());
                    dto.setEmployeeName(row.getCell(6).getStringCellValue());
                    dto.setDesignation(row.getCell(7).getStringCellValue());
                    dto.setEmploymentType(row.getCell(8).getStringCellValue());
                    dto.setDateOfJoining(row.getCell(9).getLocalDateTimeCellValue().toLocalDate());
                    dto.setSupervisorId(getLongValue(row.getCell(10)));
                    dto.setDateOfPermanent(getLocalDate(row.getCell(11)));
                    dto.setPresentAddress(row.getCell(12).getStringCellValue());
                    dto.setPermanentAddress(row.getCell(13).getStringCellValue());
                    dto.setEmployeeEmail(row.getCell(14).getStringCellValue());
                    dto.setOfficeEmail(row.getCell(15).getStringCellValue());
                    dto.setDateOfBirth(getLocalDate(row.getCell(16)));
                    dto.setReligion(row.getCell(17).getStringCellValue());
                    dto.setGender(row.getCell(18).getStringCellValue());
                    dto.setMaritalStatus(row.getCell(19).getStringCellValue());
                    dto.setBloodGroup(row.getCell(20).getStringCellValue());
                    dto.setPersonalMobile(row.getCell(21).getStringCellValue());
                    dto.setEmployeeNid(row.getCell(22).getStringCellValue());
                    dto.setStatus(row.getCell(23).getStringCellValue());
                    list.add(dto);
                } catch (Exception e) {
                    log.info("Cell: " + row.getRowNum());
                    printCellDebugInfo(row);
                    e.printStackTrace();
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
        }
    }

    private Long getLongValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case NUMERIC:
                // Excel numeric values are returned as double, so cast safely to long
                return (long) cell.getNumericCellValue();

            case STRING:
                String text = cell.getStringCellValue().trim();
                if (text.isEmpty()) return null;
                try {
                    return Long.parseLong(text);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid long value in cell: " + text, e);
                }

            case BLANK:
                return null;

            default:
                throw new RuntimeException("Unexpected cell type: " + cell.getCellType());
        }
    }

    private LocalDate getLocalDate(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case NUMERIC:
                // Excel stores dates as numeric values
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate();
                } else {
                    return null;
                }

            case STRING:
                String text = cell.getStringCellValue().trim();
                if (text.isEmpty()) return null;
                try {
                    return LocalDate.parse(text); // expects ISO format (e.g. 2025-10-29)
                } catch (Exception e) {
                    throw new RuntimeException("Invalid date format: " + text, e);
                }

            case BLANK:
                return null;

            default:
                return null;
        }
    }

    private void printCellDebugInfo(Row row) {
        StringBuilder sb = new StringBuilder("Row Data -> ");
        for (int j = 0; j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            String value = (cell == null) ? "NULL" : cell.toString();
            sb.append("[Col ").append(j).append(": ").append(value).append("] ");
        }
        System.out.println(sb.toString());
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
