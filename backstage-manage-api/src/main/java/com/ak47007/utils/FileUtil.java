package com.ak47007.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @author AK47007
 * CreateDate： 2019/10/25
 * 文件操作工具栏
 */
public class FileUtil {

    /**
     * 文件大小转换
     *
     * @param size 大小
     * @return 返回中文大小
     */
    public static String getPrintSize(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.000");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();

    }

    /**
     * 删除文件或者目录
     *
     * @param file file对象
     * @return 是否删除成功
     */
    public static boolean delFileOrFolder(File file) {
        // 如果文件不存在
        if (!file.exists()) {
            return false;
        }
        // 如果文件为目录
        if (file.isDirectory()) {
            // 获取目录下面的文件
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    delFileOrFolder(f);
                }
            }
        }
        return file.delete();
    }

    /**
     * 创建文件夹
     *
     * @param folderPath 文件夹路径
     */
    public static File createFolder(String folderPath) {
        File file = new File(folderPath);
        // 该路径不是文件夹的时候就创建文件夹
        if (!file.isDirectory()) {
            file.mkdir();
        }
        return file;
    }

    /**
     * 生成文件并写入内容
     *
     * @param filePath 文件路径
     * @param content  内容
     * @return 返回写入了内容的文件
     */
    public static File createFile(String filePath, String content) {
        filePath = fileNameFilter(filePath);
        File file = new File(filePath);
        // 该文件不存在
        if (!file.exists()) {
            try {
                // 创建文件
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 创建文件之后向文件写入内容
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
            // 写入内容
            out.write(content);
            // 关闭流
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 文件名称过滤
     *
     * @param filePath 文件路径
     * @return 返回过滤好的路径
     */
    private static String fileNameFilter(String filePath) {
        // 截取出文件名
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        fileName = fileName.replaceAll(":", "_");
        fileName = fileName.replaceAll("/", "_");
        fileName = fileName.replaceAll("\\\\", "_");
        fileName = fileName.replaceAll("\\?", "_");
        fileName = fileName.replaceAll("\"", "_");
        fileName = fileName.replaceAll("<", "_");
        fileName = fileName.replaceAll(">", "_");
        fileName = fileName.replaceAll("\\|", "_");
        // 拼接路径
        filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1) + fileName;
        return filePath;
    }
}
