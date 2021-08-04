package com.ak47007.utils;

import com.ak47007.model.addr.AdInfo;
import com.ak47007.model.addr.Response;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author AK47007
 * 获得定位
 */
@Component
public class LocationUtil {


    /**
     * 获取地址接口
     */
    private static final String GET_ADDR_API = "https://apis.map.qq.com/ws/location/v1/ip?ip=%s&key=%s";

    /**
     * 获取地址key
     */
    @Value("${ip-key}")
    private String GET_ADDR_KEY;


    /**
     * 获取登录用户的IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    /**
     * 获取位置
     *
     * @param ip ip
     * @return 响应
     */
    private Response getLocation(String ip) {
        String localIp1 = "127.0.0.1";
        String localIp2 = "localhost";
        if (localIp1.equalsIgnoreCase(ip) || localIp2.equals(ip)) {
            return null;
        }
        Response response = null;
        try {
            String doGet = HttpClientUtil.doGet(String.format(GET_ADDR_API,ip,GET_ADDR_KEY));
            response = JSONObject.parseObject(doGet, Response.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 获取省与市
     *
     * @param ip ip
     * @return 省市
     */
    public  String getPlace(String ip) {
        Response response = getLocation(ip);
        // 获取位置成功
        if (response != null && response.getStatus().equals(0)) {
            // 获取结果成功
            if (response.getResult() != null) {
                // 获取定位详细信息
                AdInfo adInfo = response.getResult().getAdInfo();
                // 外国IP
                if (adInfo.getAdCode().equals(-1)) {
                    return adInfo.getNation();
                }
                // 省与市同名说明为直辖市
                else if (adInfo.getProvince().equals(adInfo.getCity())) {
                    return adInfo.getCity();
                } else {
                    return adInfo.getProvince() + adInfo.getCity();
                }
            }
        }
        return "中国";
    }


    /**
     * 获取定位信息
     *
     * @param request 请求
     * @return 返回定位信息
     */
    public  String locationInfo(HttpServletRequest request) {
        return getPlace(getIpAddr(request));
    }


}
