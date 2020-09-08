package priv.xiean.DuerOS_dock_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("priv.xiean.DuerOS_dock_demo.dao")
@EnableCaching
@ComponentScan(basePackages = { "priv.xiean.DuerOS_dock_demo", "priv.xiean.DuerOS_dock_demo.bot",
		"priv.xiean.DuerOS_dock_demo.robot", "priv.xiean.DuerOS_dock_demo.dao",
		"priv.xiean.DuerOS_dock_demo.service.impl" })
public class DuerOS_dock_demoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DuerOS_dock_demoApplication.class, args);
	}

}
