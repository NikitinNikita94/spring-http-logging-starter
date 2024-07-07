package t1.springhttploggingstarter.http.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import t1.springhttploggingstarter.exception.HttpLoggingFilterException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "HttpLoggingFilter", urlPatterns = "/*")
@RequiredArgsConstructor
@ConditionalOnExpression("${log.enabled:true}")
public class HttpLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HttpLoggingFilter.class);

    /**
     * @param request     - HTTP запрос сервлета
     * @param response    - Ответ HTTP-сервлета
     * @param filterChain - фильтр цепочка
     * @throws ServletException - исключение
     * @throws IOException      - исключение
     */
    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {
        final ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        final ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        final StopWatch timeMeasurement = new StopWatch();

        timeMeasurement.start();
        filterChain.doFilter(requestWrapper, responseWrapper);
        timeMeasurement.stop();

        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(),
                request.getCharacterEncoding());
        String responseBody = getStringValue(responseWrapper.getContentAsByteArray(),
                response.getCharacterEncoding());

        log.info(
                "FINISHED PROCESSING : METHOD={}; REQUESTURL={}; REQUEST PAYLOAD={}; RESPONSE CODE={}; RESPONSE={}; TIM TAKEN={} milliseconds",
                request.getMethod(), request.getRequestURL(), requestBody, response.getStatus(), responseBody,
                timeMeasurement.getTotalTimeMillis());
        responseWrapper.copyBodyToResponse();
    }

    /**
     * Возвращает новую строку на основе массива байт с нужной кодировкой.
     *
     * @param contentAsByteArray - массив символов
     * @param characterEncoding  - строка с кодировкой
     * @return - новую строку
     */
    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new HttpLoggingFilterException("Unsupported encoding exception in string.", e);
        }
    }
}
