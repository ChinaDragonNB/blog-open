package com.ak47007.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author AK47007
 * @date 2020/1/29
 * Describe: 下载工具
 */
public class DownloadUtil {


    /**
     * 下载单个文件
     *
     * @param fileName 文件名称
     * @param fileType 文件类型
     * @param content  写入的内容
     * @param response 响应信息
     */
    public static void downloadFile(String fileName, String fileType, String content, HttpServletResponse response) throws IOException {
        // 临时文件路径
        String filePath = "/" + UUID.randomUUID();
        File file = FileUtil.createFile(filePath, content);
        // 下载文件
        download(file, fileName, fileType, response);
        // 最后将临时文件删除
        file.delete();
    }


    /**
     * 下载压缩文件,并给每个子文件设置文件名与内容
     *
     * @param fileInfos   文件夹下的文件信息
     * @param zipFileName zip文件名
     * @param response    响应
     */
    public static void downloadZip(List<Map<String, String>> fileInfos, String zipFileName, HttpServletResponse response) throws FileNotFoundException {
        String fileType = ".zip";
        // 创建临时文件夹
        String folderPath = "/" + System.currentTimeMillis();
        File folderFile = FileUtil.createFolder(folderPath);
        for (Map<String, String> fileInfo : fileInfos) {
            // 文件夹下的文件名称
            String fileName = fileInfo.get("fileName");
            // 文件夹下的文件内容
            String fileContent = fileInfo.get("fileContent");
            String filePath = folderPath + "/" + fileName;
            // 创建文件
            FileUtil.createFile(filePath, fileContent);
        }
        // 新创建一个zip文件
        File fileZip = new File("/" + System.currentTimeMillis() + fileType);
        FileOutputStream fos = new FileOutputStream(fileZip);
        // 压缩文件夹
        ZipUtil.toZip(folderPath, fos, true);
        // 删除文件夹
        FileUtil.delFileOrFolder(folderFile);

        // 下载文件
        try {
            download(fileZip, zipFileName, fileType, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 删除临时的zip文件
        FileUtil.delFileOrFolder(fileZip);
    }

    /**
     * 下载文件
     *
     * @param file     文件
     * @param fileName 文件名称
     * @param fileType 文件类型
     * @param response 响应信息
     */
    private static void download(File file, String fileName, String fileType, HttpServletResponse response) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        // 服务器输出流
        ServletOutputStream outputStream = getServletOutputStream(fileName, fileType, response);
        // 将文件转成字节
        byte[] buf = new byte[4096];
        int readLength;
        while (((readLength = inputStream.read(buf)) != -1)) {
            outputStream.write(buf, 0, readLength);
        }
        // 关闭输入流
        inputStream.close();
        // 刷新输出流
        outputStream.flush();
        // 关闭输出流
        outputStream.close();
    }

    /**
     * 添加请求头并得到服务器输出流
     *
     * @return 服务器输出流
     */
    private static ServletOutputStream getServletOutputStream(String fileName, String fileType, HttpServletResponse response) throws IOException {
        // 清空输出流
        response.reset();
        response.setContentType("application/octet-stream;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(fileName.getBytes("GB2312"), "8859_1")
                    + fileType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        return response.getOutputStream();
    }
}
