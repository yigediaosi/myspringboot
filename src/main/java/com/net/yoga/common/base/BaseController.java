package com.net.yoga.common.base;

import com.alibaba.fastjson.JSONObject;
import com.net.yoga.common.constant.CommonConstant;
import com.net.yoga.common.constant.CommonResult;
import com.net.yoga.util.CommonUtil;
import com.net.yoga.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class BaseController {
    public Object buildSuccJson() {
        return buildJson(CommonResult.SUCCESS, "success");
    }

    public Object buildSuccJson(Object data) {
        return buildJson(CommonResult.SUCCESS, "success", data);
    }

    public Object buildJson(int code, String msg) {
        return buildJson(code, msg, null);
    }

    public Object buildErrJson(String msg) {
        return buildJson(CommonResult.ERROR, msg, null);
    }

    public Object buildSyserrJson() {
        return buildErrJson("system error");
    }

    public Object buildJson(int code, String msg, Object data) {
        String time = DateUtil.getCurrentDate();
        if (data == null) {
            return CommonUtil.asMap("code", code, "msg", msg, "serverTime", time, "data", "");
        } else {
            return CommonUtil.asMap("code", code, "msg", msg, "serverTime", time, "data", data);
        }
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        long now = System.currentTimeMillis();//统一时间戳
        HttpServletResponse response = CommonConstant.responseTL.get();
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(JSONObject.toJSONString(buildErrJson(e.getMessage())));
        } catch (IOException ex) {
            log.warn("Error" + now + ":", ex);
        }
        log.warn("Error" + now + ":", e);
    }
}
