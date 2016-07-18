/*
 * Copyright (C) 2015 CK, Inc. All Rights Reserved.
 */

package cn.vansky.compress.shell;

import cn.vansky.framework.common.util.Tools;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * Author: CK
 * Date: 2015/4/15
 */
public abstract class CompressShell {
    protected void compress(String command) {
        try {
            Tools.executeShell(command);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void unCompress(String command) {
        try {
            Tools.executeShell(command);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void validation(String source) {
        File file = new File(source);
        if (!file.exists()) {
            throw new RuntimeException("压缩文件不存在 ：" + file.getName());
        }
    }
}
