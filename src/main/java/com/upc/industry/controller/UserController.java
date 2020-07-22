package com.upc.industry.controller;

import com.upc.industry.controller.viewobject.UserVO;
import com.upc.industry.controller.viewobject.VerifyCode;
import com.upc.industry.error.BusinessException;
import com.upc.industry.error.EmBusinessError;
import com.upc.industry.loghandler.MyLog;
import com.upc.industry.response.CommonReturnType;
import com.upc.industry.service.IVerifyCodeGenService;
import com.upc.industry.service.UserService;
import com.upc.industry.service.impl.SimpleCharVerifyCodeGenImpl;
import com.upc.industry.service.model.UserModel;
import com.upc.industry.utils.MD5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
//继承基类controller可以抛出错误异常
public class UserController{
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    //通过bean方式注入进来，似乎是一个单例模式，但是被spring bean包装托管之后本质是一个prox，其内部
    //存在一个thradelocal方式的map使用户能够在不同的线程中使用自己的session，并且thradelocal支持自清除

    @RequestMapping("/get")
    @ResponseBody
    @MyLog(value = "获取用户信息")  //这里添加了AOP的自定义注解
    public UserVO user(@RequestParam(name="id") Integer id){
        //将核心领域模型转换为可供前端UI使用的ViewObject。
        UserModel userModel = userService.getUserById(id);
        //若获取用户信息不存在，异常抛出
        return convertFromModel(userModel);
    }

    private UserVO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        //注意copy是不会将不同类型相同字段名的数据自动转换。
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

    //用户获取otp验证码短信接口
    @RequestMapping("/otpGet")
    @ResponseBody
    @MyLog(value = "给用户绑定otp验证码")  //这里添加了AOP的自定义注解
    public CommonReturnType getOtp(@RequestParam(name="telephone") String telephone){
        //按照一定规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 100000;
        String otpCode = String.valueOf(randomInt);

        //将otp验证码和用户手机号绑定.
        //使用httpsession绑定手机号和otp
        httpServletRequest.getSession().setAttribute(telephone,otpCode);
        //将OTP验证码通过短信通道发送给用户。redis天然适合使用这种验证方式。
        System.out.println("telephone = " + telephone +"&otpCode = " + otpCode);

        return CommonReturnType.create(null);
    }

    //用户注册接口
    @RequestMapping("/register")
    @ResponseBody
    @MyLog(value = "注册新用户")  //这里添加了AOP的自定义注解
    public CommonReturnType register(@RequestParam(name="telephone") String telephone,
                                     @RequestParam(name="otpCode") String otpCode,
                                     @RequestParam(name="password") String password,
                                     @RequestParam(name="name") String name,
                                     @RequestParam(name="gender") Integer gender,
                                     @RequestParam(name="age") Integer age) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号码是否和对应的otpcode相符合
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telephone);
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setTelphone(telephone);
        userModel.setAge(age);
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setEncrptPassword(MD5Utils.EncodeByMd5(password));
        userService.register(userModel);
        return CommonReturnType.create("注册成功","success");
    }

    @GetMapping("/verifyCode")
    @MyLog(value = "生成图片验证码")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        IVerifyCodeGenService iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            //设置长宽
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 28);
            String code = verifyCode.getCode();
            System.out.println(code);
            //将VerifyCode绑定session
            httpServletRequest.getSession().setAttribute("VerifyCode", code);
            //设置响应头
            response.setHeader("Pragma", "no-cache");
            //设置响应头
            response.setHeader("Cache-Control", "no-cache");
            //在代理服务器端防止缓冲
            response.setDateHeader("Expires", 0);
            //设置响应内容类型
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误");
        }
    }

    //用户注册接口
    @RequestMapping("/register2")
    @ResponseBody
    @MyLog(value = "注册新用户2")  //这里添加了AOP的自定义注解
    public CommonReturnType register2(@RequestParam(name="telephone") String telephone,
                                     @RequestParam(name="otpCode") String otpCode,
                                     @RequestParam(name="password") String password,
                                     @RequestParam(name="name") String name,
                                     @RequestParam(name="gender") Integer gender,
                                     @RequestParam(name="age") Integer age) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号码是否和对应的otpcode相符合
        String inSessionVerifyCode = (String)this.httpServletRequest.getSession().getAttribute("VerifyCode");
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionVerifyCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"图片验证码不符合");
        }
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setTelphone(telephone);
        userModel.setAge(age);
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setEncrptPassword(MD5Utils.EncodeByMd5(password));
        userService.register(userModel);
        return CommonReturnType.create("注册成功","success");
    }
}
