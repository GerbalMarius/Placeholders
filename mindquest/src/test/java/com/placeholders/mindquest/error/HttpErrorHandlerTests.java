package com.placeholders.mindquest.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpErrorHandlerTests {

    @Mock
    private HttpServletRequest servletRequest;

    @InjectMocks
    private HttpErrorHandler httpErrorHandler;

    @AfterEach
    void tearDown() {
        reset(servletRequest);
    }

    @Test
    void handleError() {

        when(servletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(null);

        String actual = httpErrorHandler.handleUnexpectedError(servletRequest);

        assertEquals("error", actual);
    }
}