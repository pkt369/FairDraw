package birdjun.profairmanager.user.service;

import birdjun.profairmanager.user.domain.Student;
import birdjun.profairmanager.user.domain.User;
import birdjun.profairmanager.user.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return studentRepository.findByNameAndUser(name, user);
    }

    public List<Student> findByUser(User user) {
        return studentRepository.findByUser(user);
    }
}
