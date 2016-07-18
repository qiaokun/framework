package cn.vansky.poi.excel.read;

import cn.vansky.poi.Constant;
import cn.vansky.poi.ReadDeal;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static cn.vansky.poi.ReadDeal.DefaultReadDeal;

/**
 * Created by IntelliJ IDEA.
 * Author: CK
 * Date: 2016/1/17
 */
public class ExcelRead<E> {
    public void read(InputStream inputStream, String fileName, ExcelReadDeal<E> deal) {
        Workbook wb = null;
        try {
            String fileType = StringUtils.substringAfterLast(fileName, ".");
            if (Constant.OFFICE_EXCEL_2003_POSTFIX.equals(fileType)) {
                wb = new HSSFWorkbook(inputStream);
            } else if (Constant.OFFICE_EXCEL_2010_POSTFIX.equals(fileType)) {
                wb = new XSSFWorkbook(inputStream);
            } else {
                throw new RuntimeException("excel file type error");
            }
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                Sheet sheet = wb.getSheetAt(i);
                if (null == sheet) {
                    continue;
                }
                int tmp = deal.getBatchCount();
                int index = 0;
                List<E> l = new ArrayList<E>(tmp);
                for (Row row : sheet) {
                    ++index;
                    if (index <= deal.skipLine()) {
                        continue;
                    }
                    E o = deal.dealBean(row);
                    if (null != o) {
                        l.add(o);
                        if (index % tmp == 0) {
                            deal.dealBatchBean(l);
                            l = new ArrayList<E>(tmp);
                        }
                    }
                }
                if (!l.isEmpty()) {
                    deal.dealBatchBean(l);
                }
            }
        } catch (Exception e) {
            // ignore
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // ignore
                    inputStream = null;
                }
            }
        }
    }

    public static interface ExcelReadDeal<E> extends ReadDeal<E> {
        /**
         * 一行excel数据返回业务BEAN
         * @param row 一行excel数据
         * @return 业务BEAN
         */
        E dealBean(Row row);
    }

    public static abstract class DefaultExcelReadDeal<E> extends DefaultReadDeal<E> implements ExcelReadDeal<E> {

    }
}
