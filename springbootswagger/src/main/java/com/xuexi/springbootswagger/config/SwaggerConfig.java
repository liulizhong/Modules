package com.xuexi.springbootswagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName SwaggerConfig
 * @create 2020-07-24 15:47
 * @Des TODO
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan(basePackages = {"com.xuexi.springbootswagger.controller"})  // 萨满秒API Controller包
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
/*        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build();*/
    }

    private ApiInfo apiInfo() {
        // 显示作者和参考文档位置，以及发邮件链接
        Contact contact = new Contact("刘立忠",
                "http://10.8.0.6:50075",
                "liulizhong@rhtect.com"
        );
        return new ApiInfoBuilder()
                .title("数仓对外Web-API接口")
                .description("springboot整合swaggerUI，提供自动化数据的管理功能！！！！")
                .contact(contact)
                .version("1.0.0")
                .build();
    }
}