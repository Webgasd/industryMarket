package com.upc.industry.service;

import com.upc.industry.controller.viewobject.UserVO;
import com.upc.industry.dataobject.UserDO;
import com.upc.industry.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel);
}
