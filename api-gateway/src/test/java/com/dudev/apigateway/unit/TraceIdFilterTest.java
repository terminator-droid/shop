package com.dudev.apigateway.unit;


import com.dudev.apigateway.security.filter.TraceIdFilter;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TraceIdFilterTest {

    private static final String TRACE_ID = "X-Trace-Id";
    private final TraceIdFilter traceIdFilter = new TraceIdFilter();

    @Test
    public void shouldAddTraceIdToMdc() throws ServletException, IOException {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();

        traceIdFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        assertThat(mockHttpServletResponse.getHeader(TRACE_ID)).isNotNull();
    }
}
