package birdjun.profairmanager.user.controller;

import birdjun.profairmanager.config.ApiResponse;
import birdjun.profairmanager.user.domain.Student;
import birdjun.profairmanager.user.domain.User;
import birdjun.profairmanager.user.dto.StudentDto;
import birdjun.profairmanager.user.service.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(value = HttpStatus.OK)
    public void create(@RequestBody StudentDto studentDto) {
        User user = (User) httpSession.getAttribute("user");
        Student student = studentDto.toEntity();
        student.initUser(user);

        studentService.save(student);
    }

    @GetMapping("/list")
    @ResponseBody
    public ApiResponse<List<StudentDto>> list() {
        User user = (User) httpSession.getAttribute("user");
        List<Student> students = studentService.findByUser(user);
        List<StudentDto> list = students.stream().map(StudentDto::fromEntity).toList();
        return ApiResponse.success(list);
    }

    @GetMapping("/list/name")
    @ResponseBody
    public ApiResponse<List<StudentDto>> listWithName(@RequestParam(value = "name") String name) {
        User user = (User) httpSession.getAttribute("user");
        List<Student> students = studentService.findByNameAndUser(name, user);
        List<StudentDto> list = students.stream().map(StudentDto::fromEntity).toList();
        return ApiResponse.success(list);
    }
}
