package com.jwt.icarus.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.jwt.icarus.values.Values;
import com.jwt.icarus.svc.JwtService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;
import org.json.XML;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonRootName(value = "JWT")
@XmlRootElement(name = "JWT")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class View {

    @JsonIgnore
    @XmlAnyElement
    private Element req_el;

    @JsonIgnore
    @XmlAnyElement
    private Element res_el;

    @XmlTransient
    private Map root;


    public View(HttpServletRequest request, org.jdom.Element item) throws Exception {
        org.jdom.Element req_el = operationReq(request);
        org.jdom.Element res_el = operationRes(request, item);
        String format = StringUtils.defaultIfEmpty(request.getParameter("format"), Values.DEFAULT_FORMAT);

        if ("json".equals(format)) {
            Map root = new HashMap<>();
            root.putAll(parseEl(req_el));
            root.putAll(parseEl(res_el));

            this.root = root;
        } else {
            this.req_el = wrapperDom(req_el);
            this.res_el = wrapperDom(res_el);
        }
    }

    private org.jdom.Element operationReq(HttpServletRequest request) {
        org.jdom.Element req_el = new org.jdom.Element("OperationRequest");
        req_el.setAttribute("path", StringUtils.defaultIfEmpty((String) request.getAttribute("ERROR_REQUEST_URI"), request.getRequestURI()));

        JwtService jwtService = new JwtService();
        jwtService.parseMap(request, req_el);

        if (StringUtils.isEmpty(req_el.getAttributeValue("format"))) {
            req_el.setAttribute("format", Values.DEFAULT_FORMAT);
        }
        return req_el;
    }

    private org.jdom.Element operationRes(HttpServletRequest request, org.jdom.Element item) {
        Map<String, String> info = Values.MAPPING_INFO.get(request.getRequestURI());

        org.jdom.Element res_el = new org.jdom.Element("OperationResponse");
        org.jdom.Element root = new org.jdom.Element(info.get("title"));
        org.jdom.Element items = new org.jdom.Element("Items");
        res_el.addContent(root);
        root.addContent(items);
        root.setAttribute("desc", StringUtils.defaultIfEmpty(info.get("desc"), ""));

        items.addContent(item);
        items.setAttribute("count", Integer.toString(items.getContentSize()));

        return res_el;
    }

    private Map parseEl(org.jdom.Element element) {
        return XML.toJSONObject(new XMLOutputter().outputString(element)).toMap();
    }

    private Element wrapperDom(org.jdom.Element element) throws Exception {
        return new DOMOutputter().output(new Document(element)).getDocumentElement();
    }

}
