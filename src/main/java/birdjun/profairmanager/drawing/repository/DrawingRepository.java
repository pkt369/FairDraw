package birdjun.profairmanager.drawing.repository;

import birdjun.profairmanager.drawing.domain.Drawing;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrawingRepository extends JpaRepository<Drawing, Long> {
    List<Drawing> findByUser_Id(Long userId, Pageable pageable);
    List<Drawing> findByNameAndUser_id(String name, Long id, Pageable pageable);
}
