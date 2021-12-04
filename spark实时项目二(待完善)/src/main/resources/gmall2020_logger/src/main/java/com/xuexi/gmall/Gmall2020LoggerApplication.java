package 项目二实时项目.gmall2020_logger.src.main.java.com.xuexi.gmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// spring boot的启动类，必须在controller类的平级或者上级，即controller类必须在他的平级或下级才能被扫描到
@SpringBootApplication
public class Gmall2020LoggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Gmall2020LoggerApplication.class, args);
    }

}
