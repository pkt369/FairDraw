package birdjun.profairmanager.common;

import birdjun.profairmanager.user.domain.*;
import birdjun.profairmanager.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserSetUp {
    private final UserRepository userRepository;

    public UserSetUp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
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

    public Student createStudent(String name, User user) {
        Student student = Student.builder()
                .name(name)
                .birth("970409")
                .gender(Gender.M)
                .disabledType(DisabledType.NONE)
                .guardianName("hong")
                .phone("01011112222")
                .guardianPhone("01033334444")
                .build();
        student.initUser(user);
        return student;
    }
}
