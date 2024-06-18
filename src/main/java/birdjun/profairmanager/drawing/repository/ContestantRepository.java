package birdjun.profairmanager.drawing.repository;

import birdjun.profairmanager.drawing.domain.Contestant;
import birdjun.profairmanager.drawing.domain.Drawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContestantRepository extends JpaRepository<Contestant, Integer> {
    List<Contestant> findByDrawing_Id(Long drawingId);
    @Query("SELECT c FROM Contestant c WHERE c.drawing IN :drawings AND c.isWinner = :isWinner")
    List<Contestant> findByAllDrawing_IdAndIsWinner(List<Drawing> drawings, Boolean isWinner);
}
