package com.ak47007.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LocationUtils {

    @Value("${ip-key}")
    private String key;


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
     * 根据IP获取定位
     *
     * @return 位置信息
     */
    public String getPlace(String ip) {
        String localIp1 = "127.0.0.1";
        String localIp2 = "localhost";
        if (localIp1.equalsIgnoreCase(ip) || localIp2.equalsIgnoreCase(ip)) {
            return "本地";
        }
        String url = "https://apis.map.qq.com/ws/location/v1/ip?ip=%s&key=%s";

        try {
            String doGet = HttpClientUtils.doGet(String.format(url, ip, key));
            JSONObject jsonObject = JSONObject.parseObject(doGet);
            //结果集
            JSONObject result = (JSONObject) jsonObject.get("result");
            //所属地信息
            JSONObject adInfo = (JSONObject) result.get("ad_info");
            //国家
            String nation = adInfo.getString("nation");
            //省份
            String province = adInfo.getString("province");
            //城市
            String city = adInfo.getString("city");
            //区域
            String district = adInfo.getString("district");

            String location = province + city + district;
            if (Strings.isNotBlank(location)) {
                return location;
            } else {
                return nation;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "本地";
    }
}
