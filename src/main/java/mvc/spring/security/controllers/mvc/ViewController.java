package mvc.spring.security.controllers.mvc;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
public class ViewController {
    @GetMapping("/start")
    public String startPage() {
        return "start_page";
    }

    @GetMapping("/user")
    public String pageForAuthenticatedUsers() {
        return "user_page";
    }

    @GetMapping("/admin")
    public String pageForAdmin() {
        return "admin_page";
    }
}