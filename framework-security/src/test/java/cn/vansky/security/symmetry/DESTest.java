/*
 * Copyright (C) 2015 CK, Inc. All Rights Reserved.
 */

package cn.vansky.security.symmetry;

import cn.vansky.framework.common.util.ISOUtil;
import cn.vansky.security.ISecurity;
import cn.vansky.security.symmetry.DES;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * Author: CK
 * Date: 2015/4/13
 */
public class DESTest {
    @Test
    public void encrypt() {
        ISecurity aes = new DES(ISOUtil.hex2byte("1111111111111111"));
        byte[] encryption = aes.encrypt(ISOUtil.hex2byte("2222222222222222"));
        System.out.println(ISOUtil.hexString(encryption));
        System.out.println(ISOUtil.hexString(aes.decrypt(encryption)));
    }

    @Test
    public void encrypt1() {
        ISecurity aes = new DES("admin@va".getBytes());
        byte [] encrypt = aes.encrypt(ISOUtil.hex2byte("000000000000A43428481AE64383964A1"));
        System.out.println(ISOUtil.hexString(encrypt));
        byte [] decrypt = aes.decrypt(encrypt);
        System.out.println(ISOUtil.hexString(decrypt));
    }
}
