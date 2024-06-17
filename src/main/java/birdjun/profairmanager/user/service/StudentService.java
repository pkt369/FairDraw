package birdjun.profairmanager.user.service;

import birdjun.profairmanager.user.domain.DisabledType;
import birdjun.profairmanager.user.domain.Gender;
import birdjun.profairmanager.user.domain.Student;
import birdjun.profairmanager.user.domain.User;
import birdjun.profairmanager.user.dto.StudentDto;
import birdjun.profairmanager.user.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

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

    public List<StudentDto> excelUpload(MultipartFile file, User user) throws Exception {
        int columnSize = 8;
        if (file.isEmpty()) {
            throw new Exception("엑셀파일이 비워져 있습니다.");
        }

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<Student> data = new ArrayList<>();

            for (Row row : sheet) {
                //header
                if (row.getRowNum() == 0) {
                    continue;
                }
                if (row.getPhysicalNumberOfCells() < columnSize) {
                    break;
                }
                data.add(this.excelRowToStudent(row, user));
            }
            List<Student> removeDuplicateStudents = this.removeDuplicateStudents(data, user);
            removeDuplicateStudents.forEach(student -> student.initUser(user));

            List<Student> students = studentRepository.saveAll(removeDuplicateStudents);
            return students.stream().map(StudentDto::fromEntity).toList();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public List<Student> removeDuplicateStudents(List<Student> data, User user) throws Exception {
        List<String> names = data.stream().map(Student::getName).toList();
        List<Student> alreadyRegisterStudents = studentRepository.findByNamesAndUser(names, user);

        Map<String, Student> map = new HashMap<>();
        for (Student student : data) {
            String key = student.getName() + student.getBirth() + student.getGender().name() + student.getDisabledType().name();
            map.put(key, student);
        }

        for (Student al : alreadyRegisterStudents) {
            String key = al.getName() + al.getBirth() + al.getGender().name() + al.getDisabledType().name();
            map.remove(key);
        }
        return map.values().stream().toList();
    }

    private Student excelRowToStudent(Row row, User user) {
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
