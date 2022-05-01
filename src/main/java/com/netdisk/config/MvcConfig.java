package com.netdisk.config;
import com.netdisk.component.LoginHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.unit.DataSize;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

@Configuration
public class MvcConfig implements WebMvcConfigurer, ErrorPageRegistrar {
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        /** 设置参数
        * @Description:
        * @Param: []
        * @return: javax.servlet.MultipartConfigElement
        * @Date: 2022/4/29
        */
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 文件大小上限
        factory.setMaxFileSize(DataSize.parse("5GB"));
        // 传输总数据大小限制
        factory.setMaxRequestSize(DataSize.parse("60GB"));
        return factory.createMultipartConfig();
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        /**
        * @Description: 注册控制器
        * @Param: [registry]
        * @return: void
        * @Date: 2022/4/29
        */
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/error400Page").setViewName("error/400");
        registry.addViewController("/error401Page").setViewName("error/401");
        registry.addViewController("/error404Page").setViewName("error/404");
        registry.addViewController("/error500Page").setViewName("error/500");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    /**
    * @Description: 添加拦截器
    * @Param: [registry]
    * @return: void
    * @Date: 2022/4/29
    */
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(
                        "/","/temp-file","/error400Page","/error401Page","/error404Page","/error500Page","/uploadTempFile","/admin","/sendCode","/loginByQQ","/login","/register","/file/share","/connection",
                        "/asserts/**","/**/*.css", "/**/*.js", "/**/*.png ", "/**/*.jpg"
                        ,"/**/*.jpeg","/**/*.gif", "/**/fonts/*", "/**/*.svg");
    }

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        /**
        * @Description: 配置错误页面
        * @Param: [registry]
        * @return: void
        * @Date: 2022/4/29
        */
        ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/error400Page");
        ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error401Page");
        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error404Page");
        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error500Page");
        registry.addErrorPages(error400Page,error401Page,error404Page,error500Page);
    }
}