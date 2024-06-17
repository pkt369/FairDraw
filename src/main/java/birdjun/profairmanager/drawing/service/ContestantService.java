package birdjun.profairmanager.drawing.service;

import birdjun.profairmanager.drawing.domain.Contestant;
import birdjun.profairmanager.drawing.domain.Drawing;
import birdjun.profairmanager.drawing.repository.ContestantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContestantService {

    private ContestantRepository contestantRepository;

    public void save(Contestant contestant) {
        contestantRepository.save(contestant);
    }

    public List<Contestant> findByDrawing(Drawing drawing) {
        return contestantRepository.findByDrawing_Id(drawing.getId());
    }
}
