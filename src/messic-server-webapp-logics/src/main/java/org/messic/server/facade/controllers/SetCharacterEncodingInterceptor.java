package org.messic.server.facade.controllers;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Sets the character encoding for the request.  Defaults to UTF-8.
 */
public class SetCharacterEncodingInterceptor extends HandlerInterceptorAdapter {
    private String characterEncoding = "UTF-8";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        request.setCharacterEncoding(this.characterEncoding);
        return true;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }
}