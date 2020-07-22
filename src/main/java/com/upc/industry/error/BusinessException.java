package com.upc.industry.error;

//包装器业务异常类实现。
public class BusinessException extends RuntimeException implements CommonError{
    //强关联一个对应的commonError，并且也是刚才的枚举类
    private CommonError commonError;
    //并且有构造函数供我们使用；
    //直接接收EmBussinessError的传参用于构造业务异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError=commonError;
    }
    //接收自定义errorMsg的方式构造业务异常
    public BusinessException(CommonError commonError,String errorMsg){
        super();
        this.commonError=commonError;
        this.commonError.setErrMsg(errorMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return null;
    }
}
