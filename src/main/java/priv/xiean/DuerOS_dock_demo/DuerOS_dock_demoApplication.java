package priv.xiean.DuerOS_dock_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "priv.xiean.DuerOS_dock_demo.bot", "priv.xiean.DuerOS_dock_demo.robot" })
@EnableCaching
@ComponentScan(basePackages = { "priv.xiean.DuerOS_dock_demo" })
public class DuerOS_dock_demoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuerOS_dock_demoApplication.class, args);
	}

}
