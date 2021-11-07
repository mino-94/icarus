package com.jwt.icarus.ctr;

import com.jwt.icarus.svc.JwtService;
import com.jwt.icarus.view.View;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class JwtController {

    @Autowired
    private JwtService jwtService;


    @PostMapping("/publish/token")
    @Description("JWT 토큰발급")
    public View publishToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map param = new HashMap<>();
        jwtService.parseMap(request, param);

        log.info("JWT Publish Request Param : " + param);

        Element item = new Element("Item");
        item.setAttribute("status", "SUCCESS");
        item.setAttribute("token", jwtService.publishToken(param));

        return new View(request, item);
    }

    @PostMapping("/subscribe/token")
    @Description("JWT 토큰인증")
    public View subscribeToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = jwtService.getToken(request);
        String status = "FAIL";

        log.info("JWT Subscribe Request Token : " + token);

        Element item = new Element("Item");

        if (StringUtils.isEmpty(token)) {
            item.setAttribute("msg", "Token is Empty");
        } else {
            Map<String, String> result = jwtService.validAuthToken(token);

            if (!result.containsKey("body")) {
                item.setAttribute("msg", result.get("msg"));
            } else {
                status = "SUCECSS";
                jwtService.parseMap(result.get("body"), item);
            }
        }
        item.setAttribute("status", status);

        String msg = item.getAttributeValue("msg");

        if (StringUtils.isNotEmpty(msg)) {
            log.error(msg);
        }
        return new View(request, item);
    }

}
