package birdjun.profairmanager.user.repository;

import birdjun.profairmanager.user.domain.DisabledType;
import birdjun.profairmanager.user.domain.Gender;
import birdjun.profairmanager.user.domain.Student;
import birdjun.profairmanager.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    public List<Student> findByName(String name);
    public List<Student> findByUser_Id(Long id);
    public List<Student> findByNameAndUser_Id(String name, Long userId);
    @Query("SELECT s FROM Student s WHERE s.name IN :names and s.user = :user")
    public List<Student> findByNamesAndUser(List<String> names, User user);
}
