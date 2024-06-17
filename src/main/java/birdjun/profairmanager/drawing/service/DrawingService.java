package birdjun.profairmanager.drawing.service;

import birdjun.profairmanager.drawing.domain.Drawing;
import birdjun.profairmanager.drawing.repository.DrawingRepository;
import birdjun.profairmanager.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrawingService {
    private final DrawingRepository drawingRepository;

    public void save(Drawing drawing) {
        drawingRepository.save(drawing);
    }

    public List<Drawing> findByUser(User user, Pageable pageable) {
        return drawingRepository.findByUser_Id(user.getId(), pageable);
    }

    public List<Drawing> findByNameAndUser(String name, User user, Pageable pageable) {
        return drawingRepository.findByNameAndUser_id(name, user.getId(), pageable);
    }
}
