package birdjun.profairmanager.drawing.service;

import birdjun.profairmanager.drawing.domain.Contestant;
import birdjun.profairmanager.drawing.domain.Drawing;
import birdjun.profairmanager.drawing.domain.dto.DrawingDto;
import birdjun.profairmanager.drawing.domain.dto.DrawingRequest;
import birdjun.profairmanager.drawing.domain.dto.DrawingResponse;
import birdjun.profairmanager.drawing.repository.ContestantRepository;
import birdjun.profairmanager.drawing.repository.DrawingRepository;
import birdjun.profairmanager.user.domain.Student;
import birdjun.profairmanager.user.domain.User;
import birdjun.profairmanager.user.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class DrawingService {
    private final DrawingRepository drawingRepository;
    private final ContestantRepository contestantRepository;
    private final StudentRepository studentRepository;

    public void save(Drawing drawing) {
        drawingRepository.save(drawing);
    }

    public List<Drawing> findByUser(User user, Pageable pageable) {
        return drawingRepository.findByUser_Id(user.getId(), pageable);
    }

    public List<Drawing> findByNameAndUser(String name, User user, Pageable pageable) {
        return drawingRepository.findByNameAndUser_id(name, user.getId(), pageable);
    }

    public DrawingResponse randomDrawing(DrawingRequest drawingRequest, User user) {
        List<Contestant> beforeWinner = contestantRepository.findByAllDrawing_IdAndIsWinner(drawingRequest.getRemoveDrawingIdList(), true);
        Map<Student, Integer> map = new HashMap<>();
        beforeWinner.forEach(contestant -> map.put(contestant.getStudent(), 1));

        List<Student> students = studentRepository.findAllById(drawingRequest.getStudentIdList());
        List<Student> attemptStudent = students.stream().filter(student -> !map.containsKey(student)).toList();

        Map<Student, Integer> winnerMap = new HashMap<>();
        while (winnerMap.size() < drawingRequest.getWinnerCount() && winnerMap.size() < attemptStudent.size()) {
            Random random = new Random();
            int index = random.nextInt(attemptStudent.size());
            winnerMap.put(attemptStudent.get(index), 1);
        }

        Drawing drawing = Drawing.builder()
                .name(drawingRequest.getName())
                .winnerCount(drawingRequest.getWinnerCount())
                .user(user)
                .build();
        drawingRepository.save(drawing);

        List<Contestant> contestantList = attemptStudent.stream().map((student) -> Contestant.builder()
                .student(student)
                .user(user)
                .drawing(drawing)
                .isWinner(winnerMap.containsKey(student))
                .build()
                ).toList();
        contestantRepository.saveAll(contestantList);

        List<Student> winner = winnerMap.keySet().stream().toList();
        List<Student> loser = attemptStudent.stream().filter(student -> !winnerMap.containsKey(student)).toList();

        return DrawingResponse.builder()
                .id(drawing.getId())
                .name(drawing.getName())
                .winnerList(winner)
                .LoserList(loser)
                .winnerCount(drawing.getWinnerCount())
                .build();
    }
}
