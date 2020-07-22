package com.upc.industry.service;

import com.upc.industry.service.model.SysErrorModel;
import com.upc.industry.service.model.SysLogModel;

public interface SysLogService {
    void save(SysLogModel sysLogModel);
    void add(SysErrorModel sysErrorModel);
}
