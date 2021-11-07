package com.jwt.icarus.conf;

import com.jwt.icarus.values.Values;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties("jwt.vals.conf")
public class ContentTypeConfig implements WebMvcConfigurer {

    private List<String> contentTypes;


    @PostConstruct
    private void init() {
        for (String type : contentTypes) {
            Values.MEDIA_TYPES.put(type, new MediaType("application", type, StandardCharsets.UTF_8));
        }
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.mediaTypes(Values.MEDIA_TYPES);
        configurer.favorParameter(true);
        configurer.ignoreAcceptHeader(true);
    }

}
