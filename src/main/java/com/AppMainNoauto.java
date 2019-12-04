package com;

import java.util.Properties;

import com.job.SapJobAll2;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.job.SapJobAll;

@SpringBootApplication
@ComponentScan("com")

//@MapperScan({"com.merck.dao","com.sap.dao"})
//@MapperScan("com.*.dao")
public class AppMainNoauto {
private static Logger log=LoggerFactory.getLogger(AppMainNoauto.class);
	
	public static final Properties p = new Properties();
	
	public static void main(String[] args) {
		//AppMainAll.getParamDetails(args);
		ConfigurableApplicationContext run = SpringApplication.run(AppMainAll.class, args);
		log.info("springBoot容器已经启动");
		SapJobAll bean = (SapJobAll) run.getBean("sapJobAll");
//		SapJobAll2 bean = (SapJobAll2) run.getBean("sapJobAll2");
		bean.run();
		run.close();
		log.info("springBoot容器已经关闭");
		
	}
	

}
