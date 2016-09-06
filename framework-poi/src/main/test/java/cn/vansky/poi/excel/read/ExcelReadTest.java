/*
 * Copyright (C) 2016 CK, Inc. All Rights Reserved.
 */

package cn.vansky.poi.excel.read;

import cn.vansky.poi.excel.write.ExcelDto;
import org.apache.poi.ss.usermodel.Row;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.util.List;

import static org.testng.Assert.*;

public class ExcelReadTest {

    @Test
    public void testRead() throws Exception {
        ExcelRead<ExcelDto> excelRead = new ExcelRead<ExcelDto>();
        excelRead.read(ExcelReadTest.class.getResourceAsStream("/a.xls"), "a.xls", new ExcelRead.DefaultExcelReadDeal<ExcelDto>() {
            public ExcelDto dealBean(Row row) {
                ExcelDto dto = new ExcelDto();
                dto.setId(Long.valueOf(row.getCell(0).toString()));
                dto.setName(row.getCell(1).toString());
                dto.setAge(Integer.valueOf(row.getCell(2).toString()));
                return dto;
            }

            public void dealBatchBean(List<ExcelDto> list) {
                System.out.println(list.size());
            }
        });
    }
}