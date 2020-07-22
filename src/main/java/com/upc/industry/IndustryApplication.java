package com.upc.industry;

import com.upc.industry.dao.UserDOMapper;
import com.upc.industry.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.upc.industry"})
@MapperScan("com.upc.industry.dao")
@RestController
@EnableScheduling
public class IndustryApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndustryApplication.class, args);
        System.out.println("hola world");
    }

}
