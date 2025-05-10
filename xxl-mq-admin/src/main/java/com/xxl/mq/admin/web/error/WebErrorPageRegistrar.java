package com.xxl.mq.admin.web.error;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * error page
 */
@Component
public class WebErrorPageRegistrar implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {

        ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/errorpage");
        ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/errorpage");
        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/errorpage");
        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/errorpage");

        registry.addErrorPages(error400Page,error401Page,error404Page,error500Page);
    }
}
