package com.open.boot;


import com.open.boot.core.context.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ServletComponentScan
@EnableCaching
@EnableScheduling
@EntityScan(basePackages = {"com.open.boot"})
public class ActorApplication {

	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(ActorApplication.class, args);
		SpringContextUtil.setApplicationContext(app);
	}
}
