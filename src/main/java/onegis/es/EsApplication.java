package onegis.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * es版本6.5.1，采用ik分词器，版本6.5.1
 *
 * @author yz
 * @date 2012/12/06
 */
//@EnableScheduling//启动定时任务
@SpringBootApplication(exclude=DataSourceAutoConfiguration.class)
@EnableFeignClients
public class EsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class, args);
    }
}
