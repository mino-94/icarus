package com.jwt.icarus.conf;

import com.jwt.icarus.values.Values;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Configuration
public class TokenBuilderConfig {

    @Value("classpath:/secret/key")
    private Resource resource;


    @PostConstruct
    private void setTokenBuilder() {
        byte[] secret_key_byte = DatatypeConverter.parseBase64Binary(loadKey());
        Values.PUBLISH_TOKEN.signWith(Keys.hmacShaKeyFor(secret_key_byte), SignatureAlgorithm.HS256);
        Values.SUBSCRIBE_TOKEN.setSigningKey(secret_key_byte);
    }

    private String loadKey() {
        String key = "";

        try {
            byte[] data = FileCopyUtils.copyToByteArray(resource.getInputStream());
            key = new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(resource + ": load fail");
        }
        return key;
    }

}
