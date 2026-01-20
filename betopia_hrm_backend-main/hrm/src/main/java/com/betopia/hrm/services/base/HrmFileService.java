package com.betopia.hrm.services.base;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface HrmFileService<T> {

    List<T> importFromExcel(MultipartFile file) throws Exception;
    ByteArrayOutputStream exportToExcel(List<T> data) throws Exception;
    ByteArrayOutputStream exportToCsv(List<T> data) throws Exception;
    ByteArrayOutputStream exportToPdf(List<T> data) throws Exception;
    Class<T> getEntityClass();
}
