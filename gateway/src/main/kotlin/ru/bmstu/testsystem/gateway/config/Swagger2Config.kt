package ru.bmstu.testsystem.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class Swagger2Config {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2).select()
            .apis(
                RequestHandlerSelectors
                    .basePackage("ru.bmstu.testsystem.gateway.web")
            )
            .paths(PathSelectors.regex("/.*"))
            .build().apiInfo(apiEndPointsInfo())
    }

    private fun apiEndPointsInfo(): ApiInfo {
        return ApiInfoBuilder().title("Spring Boot REST API")
            .description("REST API for testing system")
            .contact(Contact("Olga Yakovleva","", "olg-199774@mail.ru"))
            .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .version("1.0.0")
            .build()
    }
}
