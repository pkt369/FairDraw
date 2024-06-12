package birdjun.profairmanager.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app")
public class ApplicationController {

    @GetMapping("")
    public String index() {
        return "application/mainPage";
    }
}
