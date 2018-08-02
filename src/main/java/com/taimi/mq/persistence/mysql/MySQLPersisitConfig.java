package com.taimi.mq.persistence.mysql;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by superttmm on 27/07/2018.
 */
@Configuration
@EntityScan("com.taimi.mq.persistence.mysql")
@EnableJpaRepositories("com.taimi.mq.persistence.mysql")
@ComponentScan({"com.taimi.mq.persistence.mysql"})
public class MySQLPersisitConfig {

}
