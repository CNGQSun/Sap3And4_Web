package com;

import java.util.Properties;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.job.SapJobAll2;

@SpringBootApplication
@ComponentScan
@MapperScan("com.*.dao")
public class AppMainNoauto2 {
private static Logger log=LoggerFactory.getLogger(AppMainNoauto2.class);
	
	public static final Properties p = new Properties();
	
	public static void main(String[] args) {
		//AppMainAll.getParamDetails(args);
		ConfigurableApplicationContext run = SpringApplication.run(AppMainAll.class, args);
		log.info("springBoot容器已经启动");
		SapJobAll2 bean = (SapJobAll2) run.getBean("sapJobAll2");
		bean.run();
		run.close();
		
	}
	

}
