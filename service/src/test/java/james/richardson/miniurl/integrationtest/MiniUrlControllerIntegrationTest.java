package james.richardson.miniurl.integrationtest;

import com.jayway.jsonpath.JsonPath;
import james.richardson.miniurl.Application;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("integrationTest")
@SpringBootTest(classes = { Application.class })
@ExtendWith(SpringExtension.class)
public class MiniUrlControllerIntegrationTest {
    private final String URI = "/mini-url";

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class Post {
        @Test
        void createsNewLink() throws Exception {
            final String longUrl = "www.google.co.uk";
            createNewLink(longUrl)
                    .andExpect(jsonPath("miniUrlCode").exists())
                    .andExpect(jsonPath("longUrl").value(longUrl))
                    .andExpect(jsonPath("openCount").value("0"));
        }

        @Test
        void cannotCreateLinkToEmptyUrl() throws Exception {
            mockMvc.perform(newLinkRequest(""))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class Get {
        @Test
        void returnsNotFoundResponseIfCodeDoesNotExist() throws Exception {
            mockMvc.perform(newSearchRequest("codeDoesNotExisst"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void returnsLongUrlWhenCodeExists() throws Exception {
            final String longUrl = "www.bbc.co.uk";

            final MvcResult storeResponse = createNewLink(longUrl).andReturn();
            final String newCode = JsonPath.read(storeResponse.getResponse().getContentAsString(), "$.miniUrlCode");

            mockMvc.perform(newSearchRequest(newCode))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("miniUrlCode").value(newCode))
                    .andExpect(jsonPath("longUrl").value(longUrl))
                    .andExpect(jsonPath("openCount").value("0"));
        }
    }

    @Nested
    class Put {
        @Test
        void canUpdateOpenCountForExistingRequest() throws Exception {
            final String longUrl = "www.yahoo.com";

            final MvcResult storeResponse = createNewLink(longUrl).andReturn();
            final String newCode = JsonPath.read(storeResponse.getResponse().getContentAsString(), "$.miniUrlCode");

            final int newCount = 123;

            mockMvc.perform(newUpdateRequest(newCode, longUrl, newCount))
                    .andExpect(status().isOk());

            mockMvc.perform(newSearchRequest(newCode))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("miniUrlCode").value(newCode))
                    .andExpect(jsonPath("longUrl").value(longUrl))
                    .andExpect(jsonPath("openCount").value(newCount));
        }

        private RequestBuilder newUpdateRequest(String miniUrlCode, String longUrl, int openCount) {
            return put(URI + "/" + miniUrlCode)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"longUrl\":\"" + longUrl + "\",\"openCount\":" + openCount + "}");
        }
    }

    private RequestBuilder newSearchRequest(String miniUrlCode) {
        return get(URI + "/" + miniUrlCode);
    }

    private ResultActions createNewLink(final String longUrl) throws Exception {
        return mockMvc.perform(newLinkRequest(longUrl))
                .andExpect(status().isOk());
    }

    private MockHttpServletRequestBuilder newLinkRequest(String longUrl) {
        return post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"longUrl\":\"" + longUrl + "\"}");
    }
}
