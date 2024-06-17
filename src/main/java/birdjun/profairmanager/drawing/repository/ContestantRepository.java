package birdjun.profairmanager.drawing.repository;

import birdjun.profairmanager.drawing.domain.Contestant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestantRepository extends JpaRepository<Contestant, Integer> {
    List<Contestant> findByDrawing_Id(Long drawingId);
}
