package com.codecool.peermentoringbackend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenServices jwtTokenServices;

    JwtTokenFilter(JwtTokenServices jwtTokenServices) {
        this.jwtTokenServices = jwtTokenServices;
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenServices.getTokenFromRequest((HttpServletRequest) req);

        if (token != null && jwtTokenServices.validateToken(token)) {
            Authentication auth = jwtTokenServices.parseUserFromTokenInfo(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//
//        ((HttpServletResponse) res).setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//        ((HttpServletResponse) res).setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        ((HttpServletResponse) res).setHeader("Access-Control-Max-Age", "86400");
//
//        response.addHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
//        //response.setHeader("Set-Cookie", "locale=de; HttpOnly; SameSite=None");
//        filterChain.doFilter(req, res);
//        if ("OPTIONS".equals(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            filterChain.doFilter(req, res);
//        } else {
//            filterChain.doFilter(req, res);
//        }

       filterChain.doFilter(req, res);

    }

}
