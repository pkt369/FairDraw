package birdjun.profairmanager.user.controller;

import birdjun.profairmanager.config.ApiResponse;
import birdjun.profairmanager.user.domain.Student;
import birdjun.profairmanager.user.domain.User;
import birdjun.profairmanager.user.service.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final HttpSession httpSession;

    @PostMapping("/create")
    public void create(@RequestBody Student student) {
        studentService.save(student);
    }

    @GetMapping("/list")
    @ResponseBody
    public ApiResponse<List<Student>> list() {
        User user = (User) httpSession.getAttribute("user");
        return ApiResponse.success(studentService.findByUser(user));
    }

    @GetMapping("/list/name")
    @ResponseBody
    public List<Student> listWithName(@RequestParam(value = "name") String name) {
        User user = (User) httpSession.getAttribute("user");
        System.out.println(user.toString());
        return studentService.findByNameAndUser(name, user);
    }
}
