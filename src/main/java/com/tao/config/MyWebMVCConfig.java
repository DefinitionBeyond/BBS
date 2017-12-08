package com.tao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebMVCConfig extends WebMvcConfigurerAdapter {
    @Override //设置主页
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index");//设置当访问根的时候，转向到/index页
        super.addViewControllers(registry);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //防止static下的页面被拦截
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").resourceChain(true);
//        super.addResourceHandlers(registry);
    }
}
