package t1.springhttploggingstarter.http.rest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor
@AutoConfigureMockMvc
class LoggingControllerTest {

    public static final String URI = "/api/v1/logging/success";
    public static final String EXPECTED_HTTP_RESPONSE_CONTENT = "success";

    private final MockMvc mockMvc;

    @Test
    public void successShouldDoReturnStringResponse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(200, status);
        assertEquals(EXPECTED_HTTP_RESPONSE_CONTENT, content);
    }
}