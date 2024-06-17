package birdjun.profairmanager.user.service;

import birdjun.profairmanager.config.ApiResponse;
import birdjun.profairmanager.user.domain.DisabledType;
import birdjun.profairmanager.user.domain.Gender;
import birdjun.profairmanager.user.domain.Student;
import birdjun.profairmanager.user.domain.User;
import birdjun.profairmanager.user.dto.StudentDto;
import birdjun.profairmanager.user.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final HttpSession httpSession;

    public void save(Student student) {
        studentRepository.save(student);
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public List<Student> findByNameAndUser(String name, User user) {
        return studentRepository.findByNameAndUser_Id(name, user.getId());
    }

    public List<Student> findByUser(User user) {
        return studentRepository.findByUser_Id(user.getId());
    }

    public List<StudentDto> excelUpload(MultipartFile file) throws Exception {
        int columnSize = 8;
        if (file.isEmpty()) {
            throw new Exception("엑셀파일이 비워져 있습니다.");
        }

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<Student> data = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                if (row.getPhysicalNumberOfCells() < columnSize) {
                    break;
                }
                data.add(this.excelRowToStudent(row));
            }

            // 여기서 데이터를 처리하거나 저장할 수 있습니다.
            // 예를 들어, 데이터베이스에 저장하거나 다른 비즈니스 로직을 수행할 수 있습니다.
            List<Student> students = studentRepository.saveAll(data);
            return students.stream().map(StudentDto::fromEntity).toList();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private Student excelRowToStudent(Row row) {
        User user = (User) httpSession.getAttribute("user");
        //이름, 이메일, 생년월일, 휴대폰번호, 보호자이름, 보호자번호, 성별, 장애타입
        return Student.builder()
                .name(getCellValue(row.getCell(0)))
                .email(getCellValue(row.getCell(1)))
                .birth(getCellValue(row.getCell(2)))
                .phone(getCellValue(row.getCell(3)))
                .guardianName(getCellValue(row.getCell(4)))
                .guardianPhone(getCellValue(row.getCell(5)))
                .gender(Gender.valueOf(getCellValue(row.getCell(6))))
                .disabledType(DisabledType.valueOf(getCellValue(row.getCell(7))))
                .user(user)
                .build();
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
