package com.jwt.icarus.conf;

import com.jwt.icarus.values.Values;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RequestMappingInfoConfig {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    @PostConstruct
    private void initRequestMappingInfo() {
        Map tmp = null;
        String key = "";

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : requestMappingHandlerMapping.getHandlerMethods().entrySet()) {
            tmp = new HashMap<>();

            for (Annotation annotation : entry.getValue().getMethod().getDeclaredAnnotations()) {
                key = getValue(annotation, tmp, key);
            }

            if (!StringUtils.isEmpty(key) && !CollectionUtils.isEmpty(tmp)) {
                Values.MAPPING_INFO.put(key, tmp);
            }
        }
        log.info("Request Mapping Info : " + Values.MAPPING_INFO);
    }

    private String getValue(Annotation annotation, Map tmp, String key) {
        String[] vals = null;

        if (annotation instanceof PostMapping || annotation instanceof RequestMapping) {
            if (annotation instanceof PostMapping) {
                vals = ((PostMapping) annotation).value();
            } else {
                vals = ((RequestMapping) annotation).value();
            }

            if (vals != null && vals.length > 0) {
                key = vals[0];
                tmp.put("title", mkTitle(key));
            }
        } else if (annotation instanceof Description) {
            tmp.put("desc", ((Description) annotation).value());
        }
        return key;
    }

    private String mkTitle(String path) {
        String[] idxs = path.substring(1).split("/");
        StringBuilder builder = new StringBuilder();

        for (String idx : idxs) {
            builder.append(idx.substring(0, 1).toUpperCase());
            builder.append(idx.substring(1).toLowerCase());
        }
        return builder.toString();
    }

}
