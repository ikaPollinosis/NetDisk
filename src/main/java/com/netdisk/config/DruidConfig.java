package com.netdisk.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: DruidConfig
 * @Description: Druid配置类
 * @Date: 2022/4/29 17:47
 */

@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
    /**
    * @Description: 装载配置文件
    * @Param: []
    * @return: javax.sql.DataSource
    * @Date: 2022/4/29
    */
        return new DruidDataSource();
    }

    @Bean
    public FilterRegistrationBean webStatFilter(){
        /**
        * @Description: 设置web监控过滤器
        * @Param: []
        * @return: org.springframework.boot.web.servlet.FilterRegistrationBean
        * @Date: 2022/4/29
        */
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String,String> initParams = new HashMap<>();
        initParams.put("exclusions","*.js,*.css,/druid/*");
        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return  bean;
    }

    @Bean
    public ServletRegistrationBean statViewServlet(){

        /**
        * @Description: 配置监控Servlet
        * @Param: []
        * @return: org.springframework.boot.web.servlet.ServletRegistrationBean
        * @Date: 2022/4/29
        */

        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams = new HashMap<>();
        initParams.put("loginUsername","admin");
        initParams.put("loginPassword","200906");
        //默认就是允许所有访问
        initParams.put("allow","");
        bean.setInitParameters(initParams);
        return bean;
    }


}
