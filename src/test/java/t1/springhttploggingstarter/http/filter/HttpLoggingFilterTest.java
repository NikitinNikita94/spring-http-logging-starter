package t1.springhttploggingstarter.http.filter;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
class HttpLoggingFilterTest {
    private static final String REQUEST_URL = "\"REQUESTURL=http://localhost:8080/api/v1/logging/success; REQUEST PAYLOAD=; RESPONSE CODE=200; RESPONSE=Test response body; TIM TAKEN=0 milliseconds\"";
    private static final String PATH_TO_LOG_FILE = "logs/app.log";

    @Mock
    private Logger logger;
    private MockHttpServletRequest httpServletRequest;
    private MockHttpServletResponse httpServletResponse;
    private MockFilterChain filterChain;

    @InjectMocks
    private HttpLoggingFilter httpLoggingFilter;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        httpServletRequest = new MockHttpServletRequest("GET", REQUEST_URL);
        httpServletRequest.addHeader("Accept", "application/json");
        httpServletRequest.setContent("Test request body".getBytes());
        httpServletRequest.setContentType(MediaType.TEXT_PLAIN_VALUE);
        httpServletRequest.setCharacterEncoding("UTF-8");

        httpServletResponse = new MockHttpServletResponse();
        httpServletResponse.setContentType(MediaType.TEXT_PLAIN_VALUE);

        filterChain = new MockFilterChain();

        Files.newBufferedWriter(Path.of(PATH_TO_LOG_FILE), StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test
    void doFilterInternalShouldWriteLogToFile() throws Exception {
        doNothing().when(logger).info(any(String.class));

        httpLoggingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        File logFile = new File(PATH_TO_LOG_FILE);
        assertTrue(logFile.exists());
    }
}