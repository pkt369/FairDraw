package birdjun.profairmanager.user.repository;

import birdjun.profairmanager.user.domain.Student;
import birdjun.profairmanager.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    public List<Student> findByName(String name);
    public List<Student> findByUser(User user);
    public List<Student> findByNameAndUser(String name, User user);
}
