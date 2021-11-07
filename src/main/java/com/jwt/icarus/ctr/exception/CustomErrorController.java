package com.jwt.icarus.ctr.exception;

import com.jwt.icarus.view.View;
import lombok.extern.slf4j.Slf4j;
import org.jdom.Element;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class CustomErrorController implements ErrorController {


    @ResponseStatus
    @RequestMapping("/error")
    public View errorHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Element item = new Element("Item");
        item.setAttribute("status", "FAIL");
        item.setAttribute("msg", (String) request.getAttribute("ERROR_MESSAGE"));

        return new View(request, item);
    }

}
