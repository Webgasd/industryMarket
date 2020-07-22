package com.upc.industry.controller;

import com.upc.industry.error.BusinessException;
import com.upc.industry.error.EmBusinessError;
import com.upc.industry.response.CommonReturnType;
import com.upc.industry.service.SysLogService;
import com.upc.industry.service.model.SysErrorModel;
import com.upc.industry.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BaseController {
    @Autowired
    private SysLogService sysLogService;
    //定义exceptionhandler解决未被controller层吸收的exception。
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        ex.printStackTrace();
        Map<String,Object> responseData = new HashMap<>();
        SysErrorModel sysErrorModel = new SysErrorModel();
        if (ex instanceof BusinessException){
            //中间加处理，使用commonreturntype做处理。
            BusinessException businessException = (BusinessException)ex;
            responseData.put("errorCode",businessException.getErrCode());
            responseData.put("errMsg",businessException.getErrMsg());
            //不适用map的方式虽然断点没有问题，但是网页会报错，404，因为ExceptionHandler使用object处理
            //仅仅只能返回一个路径，做不到我们处理view类使用的responseBody这种返回。添加ResponseBody
            //会返回反序列化方式
            sysErrorModel.setIp(IpUtils.getIpAddr(request));
            //获取用户名,可以整合使用工具类ShiroUtils.getUserEntity().getUsername()
            sysErrorModel.setUserName("admin");
            sysErrorModel.setClassName(String.valueOf(businessException.getErrCode()));
            sysErrorModel.setMethodName(businessException.getErrMsg());
            sysErrorModel.setExceptionType("异常");
            sysErrorModel.setExceptionMsg(businessException.getErrMsg()); // 异常详细信息
            sysErrorModel.setOperateDate(new Date());
            sysLogService.add(sysErrorModel);
        }else {
            responseData.put("errorCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg",EmBusinessError.UNKNOWN_ERROR.getErrMsg());
            sysErrorModel.setIp(IpUtils.getIpAddr(request));
            //获取用户名,可以整合使用工具类ShiroUtils.getUserEntity().getUsername()
            sysErrorModel.setUserName("admin");
            sysErrorModel.setClassName(String.valueOf(EmBusinessError.UNKNOWN_ERROR.getErrCode()));
            sysErrorModel.setMethodName(EmBusinessError.UNKNOWN_ERROR.getErrMsg());
            sysErrorModel.setExceptionType("异常");
            sysErrorModel.setExceptionMsg(EmBusinessError.UNKNOWN_ERROR.getErrMsg()); // 异常详细信息
            sysErrorModel.setOperateDate(new Date());
            sysLogService.add(sysErrorModel);
        }
        return CommonReturnType.create(responseData,"fail");
    }
}
