package com.upc.industry.service.impl;

import com.upc.industry.dao.UserDOMapper;
import com.upc.industry.dao.UserPasswordDOMapper;
import com.upc.industry.dataobject.UserDO;
import com.upc.industry.dataobject.UserPasswordDO;
import com.upc.industry.error.BusinessException;
import com.upc.industry.error.EmBusinessError;
import com.upc.industry.service.UserService;
import com.upc.industry.service.model.UserModel;
import com.upc.industry.validator.ValidationResult;
import com.upc.industry.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        //userDO最好是不能透传给前端的，
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);
        return convertFromDataObject(userDO,userPasswordDO);
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if (userDO == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if (userPasswordDO != null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }

    @Override
    @Transactional
    public void register(UserModel userModel) {
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"空值");
        }
        ValidationResult result = validator.validate(userModel);
        if (result.isHashErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        UserDO userDO = convertFromModel(userModel);
        //try-catch有时会导致事务性处理不起作用，无法回滚。
        if(checkPhoneExist(userModel.getTelphone(),userModel.getId())) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号码已经重复");
        }
        //插入用户表
        userDOMapper.insertSelective(userDO);
        //返回赋id值，set回userModel
        userModel.setId(userDO.getId());
        //插入密码表
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    private UserDO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    public boolean checkPhoneExist(String phone,Integer id) {
        return userDOMapper.countByPhone(phone, id) > 0;
    }
}
