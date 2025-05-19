package sigma.tailoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT");
    }

    @Bean
    HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // всё, что под /api/v1/images/**, ищем сначала в classpath:/static/images/,
        // а потом — в файловой системе по абсолютному пути /home/site/wwwroot/static/images/
        registry.addResourceHandler("/api/v1/images/**")
                .addResourceLocations(
                        "classpath:/static/images/",
                        "file:/home/site/wwwroot/static/images/"
                );
        registry.addResourceHandler("/api/v1/css/**")
                .addResourceLocations(
                        "classpath:/static/css/",
                        "file:/home/site/wwwroot/static/css/"
                );

        registry.addResourceHandler("/api/v1/js/**")
                .addResourceLocations(
                        "classpath:/static/js/",
                        "file:/home/site/wwwroot/static/js/"
                );
        // аналогично для css/js, если нужно
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}