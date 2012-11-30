package JPA2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public ModelAndView test(HttpServletResponse response) throws IOException {
        return new ModelAndView("home");
    }
}
