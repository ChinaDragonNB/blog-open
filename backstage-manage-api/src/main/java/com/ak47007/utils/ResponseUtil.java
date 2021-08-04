package com.ak47007.utils;

import com.ak47007.model.base.Result;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author AK47007
 * date 2019/12/9
 * Describe: 响应处理
 */
public class ResponseUtil {

    /**
     * 输出至浏览器
     *
     * @param code     输出码
     * @param message  消息提示
     * @param response 响应信息
     */
    public static void response(int code, String message, HttpServletResponse response) {
        Result<?> result = new Result<>();
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
        result.setResultCode(code);
        result.setResultMessage(message);
        if (writer != null) {
            writer.print(JSONObject.toJSON(result));
            writer.flush();
            writer.close();
        }
    }

    /**
     * 输出至浏览器
     *
     * @param result   结果对象
     * @param response 响应信息
     */
    public static void response(Result<?> result, HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
        if (writer != null) {
            writer.print(JSONObject.toJSON(result));
            writer.flush();
            writer.close();
        }
    }
}
