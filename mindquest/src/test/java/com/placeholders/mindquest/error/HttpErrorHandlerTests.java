package com.placeholders.mindquest.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

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
    void shouldHandleUndefinedErrors() {
        when(servletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(null);
        String actual = httpErrorHandler.handleUnexpectedError(servletRequest);
        assertEquals("error", actual);
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void shouldHandleDefinedErrors(String expected, int statusCode) {
        when(servletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(statusCode);
        String actual = httpErrorHandler.handleUnexpectedError(servletRequest);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(
                        "/error/400",
                        400
                ),
                Arguments.of(
                        "/error/401",
                        401
                ),
                Arguments.of(
                        "/error/403",
                        403
                ),
                Arguments.of(
                        "/error/404",
                        404
                ),
                Arguments.of(
                        "/error/408",
                        408
                ),
                Arguments.of(
                        "/error/409",
                        409
                ),
                Arguments.of(
                        "/error/410",
                        410
                ),
                Arguments.of(
                        "/error/415",
                        415
                ),
                Arguments.of(
                        "/error/418",
                        418
                ),
                Arguments.of(
                        "/error/429",
                        429
                ),
                Arguments.of(
                        "/error/500",
                        500
                ),
                Arguments.of(
                        "/error/501",
                        501
                ),
                Arguments.of(
                        "/error/502",
                        502
                ),
                Arguments.of(
                        "/error/503",
                        503
                ),
                Arguments.of(
                        "/error/504",
                        504
                ),
                Arguments.of(
                        "/error/505",
                        505
                )
        );
    };
}