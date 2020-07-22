package com.upc.industry.service.impl;

import com.upc.industry.dao.SysErrorDOMapper;
import com.upc.industry.dao.SysLogDOMapper;
import com.upc.industry.dataobject.SysErrorDO;
import com.upc.industry.dataobject.SysLogDO;
import com.upc.industry.service.SysLogService;
import com.upc.industry.service.model.SysErrorModel;
import com.upc.industry.service.model.SysLogModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogDOMapper sysLogDOMapper;
    @Autowired
    private SysErrorDOMapper sysErrorDOMapper;

    @Override
    public void save(SysLogModel sysLogModel) {
        SysLogDO sysLogDO = new SysLogDO();
        BeanUtils.copyProperties(sysLogModel, sysLogDO);
        sysLogDOMapper.insertSelective(sysLogDO);
    }

    @Override
    public void add(SysErrorModel sysErrorModel) {
        SysErrorDO sysErrorDO = new SysErrorDO();
        BeanUtils.copyProperties(sysErrorModel, sysErrorDO);
        sysErrorDOMapper.insertSelective(sysErrorDO);
    }
}
