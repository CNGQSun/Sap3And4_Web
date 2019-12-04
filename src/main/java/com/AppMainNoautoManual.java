package com;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.job.SapJobAllManual;

@SpringBootApplication
@ComponentScan("com")
//@MapperScan({"com.merck.dao","com.sap.dao"})
//@MapperScan("com.*.dao")
public class AppMainNoautoManual {
private static Logger log=LoggerFactory.getLogger(AppMainNoautoManual.class);
	
	public static final Properties p = new Properties();
	
	public static void main(String[] args) {
		//AppMainAll.getParamDetails(args);
		ConfigurableApplicationContext run = SpringApplication.run(AppMainAll.class, args);
		log.info("springBoot容器已经启动");
		SapJobAllManual bean = (SapJobAllManual) run.getBean("sapJobAllManual");
//		SapJobAll2 bean = (SapJobAll2) run.getBean("sapJobAll2");
//		bean.run(args[0],args[1]);
		bean.run(null,null);
		run.close();
		log.info("springBoot容器已经关闭");

	}
	

}
