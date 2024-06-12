package birdjun.profairmanager.drawing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/drawing")
public class DrawingController {

    @GetMapping("/")
    public String mainPage() {
        return "drawing/mainPage";
    }
}
