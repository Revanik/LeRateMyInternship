package internshipnetwork.common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** Fast controller test: no Spring context, no database. */
class HomeControllerTest {

    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new HomeController()).build();

    @Test
    void homeRendersHomeView() throws Exception {
        mvc.perform(get("/"))
           .andExpect(status().isOk())
           .andExpect(view().name("home"))
           .andExpect(model().attributeExists("appName"));
    }
}
