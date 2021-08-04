package com.ak47007.aop;

import cn.hutool.json.JSONUtil;
import com.ak47007.annotation.OperatorLog;
import com.ak47007.enums.OperatorTypeEnum;
import com.ak47007.mapper.SysOperLogMapper;
import com.ak47007.model.SysOperLog;
import com.ak47007.model.SysUser;
import com.ak47007.model.base.Result;
import com.ak47007.utils.LocationUtil;
import com.ak47007.utils.UserUtil;
import com.alibaba.fastjson.JSON;
import org.apache.http.protocol.HTTP;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author AK47007
 * date 2021/5/19 10:51
 * describes: 切面处理类，操作日志异常日志记录处理
 */
@Aspect
@Component
public class OperatorLogAspect {

    @Resource
    private UserUtil userUtil;

    @Resource
    private SysOperLogMapper sysOperLogMapper;

    @Resource
    private LocationUtil locationUtil;

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.ak47007.annotation.OperatorLog)")
    public void logPointCut() {
    }

    @AfterReturning(value = "logPointCut()", returning = "result")
    public void saveOperLog(JoinPoint joinPoint, Object result) {

        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        OperatorLog logAnnotation = method.getAnnotation(OperatorLog.class);

        if (logAnnotation == null) {
            return;
        }

        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        SysOperLog operLog = new SysOperLog();
        operLog.setOperTime(LocalDateTime.now());

        String module = logAnnotation.operModule();
        String desc = logAnnotation.operDesc();
        OperatorTypeEnum operatorTypeEnum = logAnnotation.operType();
        operLog.setOperModule(module);
        operLog.setOperDesc(desc);
        operLog.setOperType(operatorTypeEnum.ordinal());
        operLog.setOperMethod(joinPoint.getTarget().getClass().getName() + "." + method.getName());

        operLog.setOperUrl(request.getRequestURL().toString());
        operLog.setRequestMethod(request.getMethod());

        operLog.setOperResParam(JSONUtil.toJsonStr(result));

        SysUser user = userUtil.getUser(request);
        operLog.setOperUserId(user.getId());
        operLog.setOperNickName(user.getNickName());

        String ipAddr = LocationUtil.getIpAddr(request);
        operLog.setOperIp(ipAddr);
        String location = locationUtil.getPlace(ipAddr);
        operLog.setOperLocation(location);


        String param = null;

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.isEmpty()) {
            param = argsArrayToString(joinPoint.getArgs());
        } else {
            param = JSONUtil.toJsonStr(parameterMap);
        }
        operLog.setOperReqParam(param);

        sysOperLogMapper.insert(operLog);
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    try {
                        Object jsonObj = JSON.toJSON(paramsArray[i]);
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
                return iter.next() instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
