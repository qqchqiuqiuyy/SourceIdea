package cn.bb.sourceideamanage;

import net.sf.json.JSONObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//开启定时任务
@EnableScheduling
//开启缓存
@EnableCaching
//开启异步任务
@EnableAsync
public class SourceideamanageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SourceideamanageApplication.class, args);
    }


    @Bean
    @Scope("prototype")
    public JSONObject jsonObject(){
        return new JSONObject();
    }
}

