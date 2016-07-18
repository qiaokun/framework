/*
 * Copyright (C) 2015 CK, Inc. All Rights Reserved.
 */

package cn.vansky.compress;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Author: CK
 * Date: 2015/4/14
 */
public abstract class CommonCompress implements Compress {

    /**
     * 标识 0:文件夹，1:文件，2:HttpServletResponse
     */
    protected int flag;
    /**
     * 实现接口
     */
    protected CommonCompress commonCompress;
    /**
     * the compress type
     */
    protected String type;
    /**
     * the compress type suffix
     */
    protected String suffix;
    /**
     * the source file
     */
    protected String source;
    /**
     * the target file
     */
    protected String target;
    /**
     * the source file
     */
    protected File sourceFile;
    /**
     * the target file
     */
    protected File targetFile;
    /**
     * the HttpServletResponse
     */
    protected HttpServletResponse httpServletResponse;

    /**
     * 公共压缩调用
     * @return 输出路径字符串
     */
    public void compress() {
        if (2 != this.flag) {
            validation(false);
        }
        setFlag(flag);
        commonCompress.compress();
    }

    /**
     * 解压
     * @return 输出路径字符串
     */
    public String unCompress() {
        validation(true);
        return commonCompress.unCompress();
    }

    /**
     * 验证
     * @param flag false:压缩 true:解压缩
     */
    private void validation(boolean flag) {
        // 获取源文件路径
        String sourcePath = getSource();
        String targetPath = getTarget();
        if (StringUtils.isBlank(sourcePath)) {
            throw new RuntimeException("source file name[" + sourcePath + "] no exist");
        }
        String suffix = getSuffix();
        if (!flag && targetPath != null && !targetPath.endsWith(suffix)) {
            // 判断解析类型
            throw new RuntimeException("current class compress type [" + suffix + "] source file suffix error");
        }
        File source = getSourceFile();
        if (!source.exists()) {
            throw new RuntimeException("source file name[" + sourcePath + "] no exist");
        }
        dealTarget(flag);
        if (flag) {
            if (SUFFIX_BZIP2.equals(suffix) && (StringUtils.isBlank(targetPath)
                    || targetPath.startsWith(source.getParent()) || source.getParent().startsWith(targetPath))) {
                throw new RuntimeException(SUFFIX_BZIP2 + "后缀必须给出输出目录或目标目录与源文件目录不能在一个父目录上");
            }
        }
    }

    private void dealTarget(boolean flag) {
        File source = getSourceFile();
        if (StringUtils.isBlank(getTarget())) {
            // 目标路径不存在,(压缩/解压)设置目标目录为父目录
            setTarget(source.getParent());
            File target = null;
            if (flag) {
                // 解压
                target = source.getParentFile();
            } else {
                // 压缩,文件夹,名称同文件夹名文件:文件,名称同文件名
                String fileName = source.isDirectory() ? source.getName() :
                        StringUtils.substringBeforeLast(source.getName(), DOT);
                target = new File(source.getParentFile(), fileName + DOT + getSuffix());
            }
            setTargetFile(target);
        } else {
            // 解压时,目标路径必须是文件夹,压缩时,存在文件不覆盖报错
            File target = getTargetFile();
            if (flag && target.getName().endsWith(getSuffix())) {
                throw new RuntimeException(" target must is dir not file ");
            }
            if (target.getName().endsWith(getSuffix())) {
                // 指定文件名
                if (target.exists()) {
                    throw new RuntimeException("target file exist");
                } else {
                    // 目标不存在,创建
                    setTarget(target.getAbsolutePath());
                    setTargetFile(target);
                }
            } else {
                // 指定目录
                if (!target.exists()) {
                    // 目标不存在,创建
                    File file = new File(target, File.separator);
                    file.mkdirs();
                    setTarget(target.getAbsolutePath());
                    setTargetFile(target);
                }
            }
        }
    }

    /**
     * 设置源文件名称
     * @param source 源名称
     */
    public CommonCompress source(String source) {
        setSource(source);
        setSourceFile(new File(source));
        return this;
    }

    /**
     * 设置目标文件名称
     * @param target 目标名称
     */
    public CommonCompress target(String target) {
        setTarget(target);
        setTargetFile(new File(target));
        return this;
    }

    /**
     * 设置响应
     * @param response 响应
     */
    public CommonCompress response(HttpServletResponse response) {
        this.flag = 2;
        response.reset();
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("content-disposition", "attachment; filename=" + new Date().getTime() + DOT + getSuffix());
        setHttpServletResponse(response);
        return this;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }
}
