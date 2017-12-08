package com.tao;

import com.tao.filter.encodingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan //组件为servlet的装配
public class DemoApplication {

    /**
     * 设置过滤器编号
     * 过滤内容
     * 对哪些目录下进行过滤
     * 设置过滤器名字
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean registrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(this.encodingFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("encode", "utf-8");
        filterRegistrationBean.setName("EncodingFilter");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    /**
     * 过滤器装载
     *
     * @return
     */
    @Bean(name = "EncodingFilter")
    public encodingFilter encodingFilter() {
        return new encodingFilter();
    }

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
