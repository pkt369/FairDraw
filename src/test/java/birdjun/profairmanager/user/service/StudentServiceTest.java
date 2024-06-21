package birdjun.profairmanager.user.service;

import birdjun.profairmanager.user.domain.*;
import birdjun.profairmanager.user.repository.StudentRepository;
import birdjun.profairmanager.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Rollback
class StudentServiceTest {

    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository);
    }

    @Test
    @DisplayName("학생이 저장 가능해야 한다.")
    public void givenGivenStudentWhenCreateThenSuccess() {
        //given
        User user = createUser("user");
        userRepository.save(user);
        Student student = createStudent("student");
        student.initUser(user);

        //when
        studentService.save(student);

        //then
        List<Student> students = studentService.findAll();
        assertThat(students).hasSize(1);
        assertThat(students.getFirst().getName()).isEqualTo(student.getName());
    }

    @Test
    @DisplayName("내가 저장한 학생들만 데이터를 가져와야 한다.")
    public void givenGivenStudentWhenGetListThenMyStudent() {
        //given
        User user1 = createUser("user1");
        userRepository.save(user1);
        Student student1 = createStudent("student1");
        student1.initUser(user1);

        User user2 = createUser("user2");
        userRepository.save(user2);
        Student student2 = createStudent("student2");
        student2.initUser(user2);

        //when
        studentService.save(student1);
        studentService.save(student2);

        //then
        List<Student> students = studentService.findByUser(user1);
        assertThat(students).hasSize(1);
        assertThat(students.getFirst().getName()).isEqualTo(student1.getName());
    }

    @Test
    @DisplayName("내가 저장한 학생들과 검색으로 가져온 이름의 데이터만 가져와야 한다.")
    public void givenGivenStudentWhenGetListNameThenMyStudent() {
        //given
        User user1 = createUser("user1");
        userRepository.save(user1);
        Student student1 = createStudent("student1");
        student1.initUser(user1);

        User user2 = createUser("user2");
        userRepository.save(user2);
        Student student2 = createStudent("student2");
        student2.initUser(user2);

        //when
        studentService.save(student1);
        studentService.save(student2);

        //then
        studentService.findAll().forEach(System.out::println);

        List<Student> students = studentService.findByNameAndUser(student1.getName(), user1);
        assertThat(students).hasSize(1);
        assertThat(students.getFirst().getName()).isEqualTo(student1.getName());
    }

    @Test
    @DisplayName("내가 저장한 학생들이 없을 경우 데이터는 없어야 한다.")
    public void givenNoMyStudentWhenGetListNameThenNoStudent() {
        //given
        User user1 = createUser("user1");
        userRepository.save(user1);
        Student student1 = createStudent("student1");
        student1.initUser(user1);
        Student student2 = createStudent("student2");
        student2.initUser(user1);

        User user2 = createUser("user2");
        userRepository.save(user2);


        //when
        studentService.save(student1);
        studentService.save(student2);

        //then
        List<Student> students = studentService.findByUser(user2);
        assertThat(students).hasSize(0);
    }

    @Test
    @DisplayName("이름, 생년월일, 성별, 장애타입까지 중복이면 중복된 학생으로 보고 중복으로 저장하지 않는다.")
    public void givenSameStudent_whenSaveStudent_thenNoSaveStudent() throws Exception {
        //given
        User user1 = createUser("user1");
        userRepository.save(user1);
        Student student1 = createStudent("student1");
        student1.initUser(user1);
        Student student2 = createStudent("student2");
        student2.initUser(user1);

        studentService.save(student1);
        studentService.save(student2);

        Student duplicateUser = createStudent("student1");
        Student notDuplicateUser = createStudent("new_student");
        List<Student> students = new ArrayList<>();
        students.add(duplicateUser);
        students.add(notDuplicateUser);

        //when
        List<Student> removedDuplicateStudents = studentService.removeDuplicateStudents(students, user1);

        //then
        assertThat(removedDuplicateStudents).hasSize(1);
        assertThat(removedDuplicateStudents.getFirst()).isEqualTo(notDuplicateUser);
    }

    public User createUser(String name) {
        return new User(name, "aaa@naver.com", "aaaaaaa", Role.USER);
    }

    public Student createStudent(String name) {
        return Student.builder()
                .name(name)
                .birth("970409")
                .gender(Gender.M)
                .disabledType(DisabledType.NONE)
                .guardianName("hong")
                .phone("01011112222")
                .guardianPhone("01033334444")
                .build();
    }

}