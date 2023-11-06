package com.example.project;

import com.example.project.DataHolder.MyDataSingleton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
public class ProjectApplication {

    public static void main(String[] args) throws Exception {
        MyDataSingleton myDataSingleton = new MyDataSingleton();
        SpringApplication.run(ProjectApplication.class, args);



    }

}
