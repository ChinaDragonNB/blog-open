package com.ak47007.utils;

import java.security.MessageDigest;

/**
 * 短字符生成
 */
public class ShortStrUtil {

    public static String shortStr(String str) {
        // 可以自定义生成 MD5 加密字符传前的混合 KEY
        String key = "ak47007";
        // 要使用生成 URL 的字符
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"

        };
        // 对传入字符进行 MD5 加密
        String hex = md5ByHex(key + str);

        // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
        String sTempSubString = hex.substring(8, 8 + 8);

        // 这里需要使用 long 型来转换，因为 Integer .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用long ，则会越界
        long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
        StringBuilder outChars = new StringBuilder();
        for (int j = 0; j < 6; j++) {
            // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
            long index = 0x0000003D & lHexLong;
            // 把取得的字符相加
            outChars.append(chars[(int) index]);
            // 每次循环按位右移 5 位
            lHexLong = lHexLong >> 5;
        }
        return outChars.toString();
    }

    /**
     * MD5加密(32位大写)
     *
     * @param src 加密前的字符串
     * @return 加密后的字符串
     */
    public static String md5ByHex(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = src.getBytes();
            md.reset();
            md.update(b);
            byte[] hash = md.digest();
            String hs = "";
            String stmp = "";
            for (int i = 0; i < hash.length; i++) {
                stmp = Integer.toHexString(hash[i] & 0xFF);
                if (stmp.length() == 1)
                    hs = hs + "0" + stmp;
                else {
                    hs = hs + stmp;
                }
            }
            return hs.toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }

}
