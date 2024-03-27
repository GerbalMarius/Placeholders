package com.placeholders.mindquest.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HttpErrorHandler implements ErrorController {

     @GetMapping("/error")
    public String handleUnexpectedError(HttpServletRequest request){
        String errorPage = "error";//default

         var status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

         if (status != null){

             int statusCode = Integer.parseInt(status.toString());

             switch (HttpStatus.valueOf(statusCode)){
                 case BAD_REQUEST -> errorPage = "/error/400";
                 case UNAUTHORIZED -> errorPage = "/error/401";
                 case FORBIDDEN -> errorPage = "/error/403";
                 case NOT_FOUND -> errorPage = "/error/404";
                 case REQUEST_TIMEOUT -> errorPage = "/error/408";
                 case CONFLICT -> errorPage = "/error/409";
                 case GONE -> errorPage = "/error/410";
                 case UNSUPPORTED_MEDIA_TYPE -> errorPage = "/error/415";
                 case I_AM_A_TEAPOT -> errorPage = "/error/418";
                 case TOO_MANY_REQUESTS -> errorPage = "/error/429";
                 case INTERNAL_SERVER_ERROR -> errorPage = "/error/500";
                 case NOT_IMPLEMENTED -> errorPage = "/error/501";
                 case BAD_GATEWAY -> errorPage = "/error/502";
                 case SERVICE_UNAVAILABLE -> errorPage = "/error/503";
                 case GATEWAY_TIMEOUT -> errorPage = "/error/504";
                 case HTTP_VERSION_NOT_SUPPORTED -> errorPage = "/error/505";
             }
         }
         return errorPage;
    }
}
