package JPA2.tests;

import JPA2.controller.HomeController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Comprehensive tests for HomeController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@WebAppConfiguration
public class HomeControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testHomeControllerInstantiation() {
        HomeController controller = new HomeController();
        assertNotNull(controller);
    }

    @Test
    public void testRootEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    public void testRootEndpointReturnsModelAndView() throws Exception {
        HomeController controller = new HomeController();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ModelAndView mav = controller.test(response);

        assertNotNull(mav);
        assertEquals("home", mav.getViewName());
    }

    @Test
    public void testRootEndpointWithMockMvc() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeCount(0));
    }

    @Test
    public void testResponseNotNull() throws Exception {
        HomeController controller = new HomeController();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ModelAndView mav = controller.test(response);

        assertNotNull(mav);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
    }
}
