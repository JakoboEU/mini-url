package james.richardson.miniurl.integrationtest;

import james.richardson.miniurl.Application;
import james.richardson.miniurl.Roles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ActiveProfiles("integrationTest")
@SpringBootTest(classes = { Application.class })
@ExtendWith(SpringExtension.class)
public class AdminAuditControllerIntegrationTest {
    private final String URI = "/admin/audit";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void cannotFetchWithoutCorrectRole() throws Exception {
        mockMvc.perform(searchAuditRequest(0))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {Roles.ADMIN_USER})
    void fetchesResultsByPage() throws Exception {
        mockMvc.perform(searchAuditRequest(1))
                .andExpect(status().isOk());
    }

    private RequestBuilder searchAuditRequest(int page) {
        return get(URI + "?page=" + page);
    }
}
