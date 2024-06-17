package birdjun.profairmanager.user.controller;

import birdjun.profairmanager.config.ApiResponse;
import birdjun.profairmanager.user.domain.Student;
import birdjun.profairmanager.user.domain.User;
import birdjun.profairmanager.user.dto.StudentDto;
import birdjun.profairmanager.user.service.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final HttpSession httpSession;

    @PostMapping("/create")
    @ResponseBody
    public ApiResponse<String> create(@RequestBody StudentDto studentDto) {
        User user = (User) httpSession.getAttribute("user");
        Student student = studentDto.toEntity();
        student.initUser(user);

        studentService.save(student);
        return ApiResponse.success("ok");
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

    @PostMapping("/create/excel/upload")
    @ResponseBody
    public ApiResponse<List<StudentDto>> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
        User user = (User) httpSession.getAttribute("user");
        List<StudentDto> students = studentService.excelUpload(file, user);
        return ApiResponse.success(students);
    }
}
