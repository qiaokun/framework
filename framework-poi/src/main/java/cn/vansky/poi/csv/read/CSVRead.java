package cn.vansky.poi.csv.read;

import au.com.bytecode.opencsv.CSVReader;
import cn.vansky.poi.ReadDeal;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static cn.vansky.poi.ReadDeal.DefaultReadDeal;

/**
 * Created by IntelliJ IDEA.
 * Author: CK
 * Date: 2016/1/17
 */
public class CSVRead<E> {
    public void read(InputStream inputStream, CSVReaderDeal<E> deal) {
        CSVReader reader = new CSVReader(new InputStreamReader(new DataInputStream(inputStream)));
        try {
            int tmp = deal.getBatchCount();
            List<E> l = new ArrayList<E>(tmp);
            int i = 0;
            String [] arr;
            while ((arr = reader.readNext()) != null) {
                ++i;
                if (i <= deal.skipLine()) {
                    continue;
                }
                E o = deal.dealBean(arr);
                if (o != null) {
                    l.add(o);
                    if (i % tmp == 0) {
                        deal.dealBatchBean(l);
                        l = new ArrayList<E>(tmp);
                    }
                }
            }
            if (!l.isEmpty()) {
                deal.dealBatchBean(l);
            }
        } catch (IOException e) {
            // ignore
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // ignore
                reader = null;
            }
        }
    }

    public static interface CSVReaderDeal<E> extends ReadDeal<E> {
        /**
         * 一行CSV数据返回业务BEAN
         * @param arr 一行CSV数据
         * @return 业务BEAN
         */
        E dealBean(String [] arr);
    }

    public static abstract class DefaultCSVReaderDeal<E> extends DefaultReadDeal<E> implements CSVReaderDeal<E> {

    }
}
