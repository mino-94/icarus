package com.jwt.icarus.ctr.exception;

import com.jwt.icarus.view.View;
import lombok.extern.slf4j.Slf4j;
import org.jdom.Element;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestController
@RestControllerAdvice
public class CustomExceptionController {


    @ResponseStatus
    @ExceptionHandler(Exception.class)
    public View errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        Element item = new Element("Item");
        item.setAttribute("status", "FAIL");
        item.setAttribute("msg", e.getMessage());

        request.setAttribute("ERROR_REQUEST_URI", "/error");

        log.error(printStackTrace(e));

        return new View(request, item);
    }

    private String printStackTrace(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

}
