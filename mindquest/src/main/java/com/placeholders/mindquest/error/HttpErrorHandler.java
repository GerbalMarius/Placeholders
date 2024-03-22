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
                 case NOT_FOUND -> errorPage = "/error/404";
                 case FORBIDDEN -> errorPage = "/error/403";
                 case INTERNAL_SERVER_ERROR -> errorPage = "/error/500";
             }
         }
         return errorPage;
    }
}
