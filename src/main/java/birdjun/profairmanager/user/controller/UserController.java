package birdjun.profairmanager.user.controller;

import birdjun.profairmanager.user.domain.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user/login")
    public String login() {
        return "loginPage";
    }
}
