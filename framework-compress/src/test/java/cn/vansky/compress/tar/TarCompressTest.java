/*
 * Copyright (C) 2015 CK, Inc. All Rights Reserved.
 */

package cn.vansky.compress.tar;

import cn.vansky.compress.Compress;
import org.testng.annotations.Test;

public class TarCompressTest {

    public static Compress compress = new TAR();

    @Test
    public void testCompress() {
        // 压缩目录到当前目录
        compress.source("D:\\demo");
        compress.compress();
    }

    @Test
    public void testCompress1() {
        // 压缩文件到当前目录
        compress.source("D:\\demo\\test.js");
        compress.compress();
    }

    @Test
    public void testCompress2() {
        // 压缩目录到指定文件名
        compress.source("D:\\demo");
        compress.target("D:\\2\\a.tar");
        compress.compress();
    }

    @Test
    public void testCompress3() {
        // 压缩文件到指定文件名
        compress.source("D:\\demo\\test.js");
        compress.target("D:\\2\\a.tar");
        compress.compress();
    }

    @Test
    public void testUnCompress() throws Exception {
        // 解压到当前目录
        compress.source("D:\\demo.tar");
        compress.unCompress();
    }

    @Test
    public void testUnCompress1() throws Exception {
        // 解压到指定文件夹
        compress.source("D:\\demo.tar");
        compress.target("D:\\2\\");
        compress.unCompress();
    }
}