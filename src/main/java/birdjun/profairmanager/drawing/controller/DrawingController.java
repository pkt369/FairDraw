package birdjun.profairmanager.drawing.controller;

import birdjun.profairmanager.config.ApiResponse;
import birdjun.profairmanager.drawing.domain.Drawing;
import birdjun.profairmanager.drawing.domain.dto.DrawingRequest;
import birdjun.profairmanager.drawing.domain.dto.DrawingResponse;
import birdjun.profairmanager.drawing.service.DrawingService;
import birdjun.profairmanager.user.domain.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/drawing")
public class DrawingController {

    private final DrawingService drawingService;
    private final HttpSession httpSession;

    @GetMapping("")
    public String mainPage() {
        return "drawing/search";
    }

    @GetMapping("/create")
    public String createPage() {
        return "drawing/create";
    }

    @PostMapping("/create")
    @ResponseBody
    public ApiResponse<DrawingResponse> create(@Valid @RequestBody DrawingRequest drawingRequest) {
        User user = (User) httpSession.getAttribute("user");
        DrawingResponse drawingResponse = drawingService.randomDrawing(drawingRequest, user);
        return ApiResponse.success(drawingResponse);
    }

    @GetMapping("/list")
    @ResponseBody
    public ApiResponse<List<DrawingResponse>> list(@PageableDefault Pageable pageable) {
        User user = (User) httpSession.getAttribute("user");
        List<Drawing> list = drawingService.findByUser(user, pageable);
        return ApiResponse.success(list.stream().map(DrawingResponse::toResponse).toList());
    }

    @GetMapping("/list/name")
    @ResponseBody
    public ApiResponse<List<DrawingResponse>> listByName(@PageableDefault Pageable pageable,
                                                    @RequestParam String name) {
        User user = (User) httpSession.getAttribute("user");
        List<Drawing> list = drawingService.findByNameAndUser(name, user, pageable);
        return ApiResponse.success(list.stream().map(DrawingResponse::toResponse).toList());
    }
}
