package com.guzzservices.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxUtil {
    
    public static void sendHtml(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        
        response.getWriter().print(msg);
        response.flushBuffer();
    }
    
    public static void sendText(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        
        response.getWriter().print(msg);
        response.flushBuffer();
    }

    public static void sendXml(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException {
        response.setContentType("text/xml; charset=UTF-8");
        
        response.getWriter().print(msg);
        response.flushBuffer();
    }
    
}
