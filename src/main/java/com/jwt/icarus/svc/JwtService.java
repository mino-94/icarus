package com.jwt.icarus.svc;

import com.jwt.icarus.values.Values;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.Data;
import org.jdom.Element;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Service
@ConfigurationProperties("jwt.vals")
public class JwtService {

    private int expireSec;


    public String publishToken(Map param) {
        Claims claims = setClaims(Jwts.claims(), param);
        setExpire(claims);

        return Values.PUBLISH_TOKEN.setClaims(claims).compact();
    }

    public String getToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    public Map validAuthToken(String token) {
        Map map = new HashMap<>();

        try {
            Jws<Claims> jws = Values.SUBSCRIBE_TOKEN.build().parseClaimsJws(token);
            map.put("body", jws.getBody());
        } catch (SignatureException sign) {
            map.put("msg", "Signature Exception");
        } catch (MalformedJwtException mal) {
            map.put("msg", "Malformed Jwt Token Exception");
        } catch (UnsupportedJwtException sup) {
            map.put("msg", "Unsupported Jwt token Exception");
        } catch (ExpiredJwtException exp) {
            map.put("msg", "Expired End Jwt Token Exception");
        } catch (Exception e) {
            map.put("msg", e.toString());
        }
        return map;
    }

    public void parseMap(Object obj, Object obj2) {
        Map maps = null;
        Set<String> keys = null;
        Object value = "";

        if (obj instanceof HttpServletRequest) {
            maps = ((HttpServletRequest) obj).getParameterMap();
        } else {
            maps = ((Map) obj);
        }
        keys = maps.keySet();

        for (String key : keys) {
            obj = maps.get(key);

            if (obj instanceof String[]) {
                value = ((String[]) maps.get(key))[0];
            } else {
                value = maps.get(key);
            }

            if (obj2 instanceof Map) {
                ((Map) obj2).put(key, value);
            } else {
                ((Element) obj2).setAttribute(key, value.toString());
            }
        }
    }

    private Claims setClaims(Claims jwtClaims, Map<String, String> param) {
        Claims claims = jwtClaims.setSubject("JWT(JSON Web Token)");
        Set<String> keys = param.keySet();
        String value = "";

        for (String key : keys) {
            value = param.get(key);

            if ("issuer".equals(key)) {
                claims.setIssuer(value);
            } else if ("audience".equals(key)) {
                claims.setAudience(value);
            } else if (!"format".equals(key)) {
                claims.put(key, param.get(key));
            }
        }
        return claims;
    }

    private void setExpire(Claims claims) {
        Date isuDate = new Date();
        Date expireDate = new Date();
        expireDate.setTime(isuDate.getTime() + (expireSec * 1000));

        claims.setIssuedAt(isuDate).setExpiration(expireDate);
    }

}
