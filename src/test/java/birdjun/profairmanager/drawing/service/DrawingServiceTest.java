package birdjun.profairmanager.drawing.service;

import birdjun.profairmanager.common.UserSetUp;
import birdjun.profairmanager.drawing.domain.Drawing;
import birdjun.profairmanager.drawing.domain.dto.DrawingDto;
import birdjun.profairmanager.drawing.domain.dto.DrawingResponse;
import birdjun.profairmanager.drawing.repository.ContestantRepository;
import birdjun.profairmanager.drawing.repository.DrawingRepository;
import birdjun.profairmanager.user.domain.Student;
import birdjun.profairmanager.user.domain.User;
import birdjun.profairmanager.user.repository.StudentRepository;
import birdjun.profairmanager.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class DrawingServiceTest {

    private DrawingService drawingService;
    @Autowired
    private DrawingRepository drawingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContestantRepository contestantRepository;
    @Autowired
    private StudentRepository studentRepository;

    private UserSetUp userSetUp;


    @BeforeEach
    void setUp() {
        drawingService = new DrawingService(drawingRepository, contestantRepository, studentRepository);
        userSetUp = new UserSetUp(userRepository);
    }


    @Test
    @DisplayName("추첨 생성 요청을 하면 생성이 되어야 한다.")
    public void givenDrawing_whenCallCreateApi_thenSuccess() throws Exception {
        //given
        User user = userSetUp.createUser("user1");
        userSetUp.save(user);

        Drawing drawing = createDrawing("drawing1", user);

        //when
        drawingService.save(drawing);

        //then
        List<Drawing> list = drawingService.findByUser(user, PageRequest.of(0, 10));
        assertEquals(1, list.size());
        assertEquals(drawing, list.getFirst());
    }

    @Test
    @DisplayName("추첨 리스트 요청을 하면 유저의 리스트를 보여줘야 한다")
    public void givenDrawing_whenCallListApi_thenReturnDrawingList() throws Exception {
        //given
        User user1 = userSetUp.createUser("user1");
        userSetUp.save(user1);
        User user2 = userSetUp.createUser("user2");
        userSetUp.save(user2);

        Drawing drawing1 = createDrawing("drawing1", user1);
        drawingService.save(drawing1);

        Drawing drawing2 = createDrawing("drawing2", user2);
        drawingService.save(drawing2);

        //when
        List<Drawing> list = drawingService.findByUser(user1, PageRequest.of(0, 10));

        //then
        assertEquals(1, list.size());
        assertEquals(drawing1, list.getFirst());
    }

    @Test
    @DisplayName("추첨 리스트 요청과 이름을 주면 유저의 리스트와 이름에 맞는 사람을 보여줘야 한다")
    public void givenDrawing_whenListNameFunc_thenReturnDrawingList() throws Exception {
        //given
        User user1 = userSetUp.createUser("user1");
        userSetUp.save(user1);
        User user2 = userSetUp.createUser("user2");
        userSetUp.save(user2);

        Drawing drawing1 = createDrawing("drawing1", user1);
        drawingService.save(drawing1);

        Drawing drawing2 = createDrawing("drawing2", user2);
        drawingService.save(drawing2);

        //when
        List<Drawing> list = drawingService.findByNameAndUser(drawing1.getName(), user1, PageRequest.of(0, 10));

        //then
        assertEquals(1, list.size());
        assertEquals(drawing1, list.getFirst());
    }

    @Test
    @DisplayName("추첨 리스트 요청과 이름을 주었을때 유저가 만든 정보가 하나도 없으면 아무것도 나오지 않아야 한다.")
    public void givenNoDrawing_whenListNameFunc_thenReturnNothing() throws Exception {
        //given
        User user1 = userSetUp.createUser("user1");
        userSetUp.save(user1);
        User user2 = userSetUp.createUser("user2");
        userSetUp.save(user2);

        Drawing drawing1 = createDrawing("drawing1", user2);
        drawingService.save(drawing1);

        Drawing drawing2 = createDrawing("drawing2", user2);
        drawingService.save(drawing2);

        //when
        List<Drawing> list = drawingService.findByNameAndUser(drawing1.getName(), user1, PageRequest.of(0, 10));

        //then
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("추첨 시 이전 추첨 당첨자 제거 없이 추첨시 성공적으로 뽑혀야 한다.")
    public void givenDrawingParams_whenRandomDrawing_thenReturnDrawingResponse() {
        //given
        User user1 = userSetUp.createUser("user1");
        userSetUp.save(user1);

        List<Student> students = new ArrayList<>();
        students.add(userSetUp.createStudent("student1", user1));
        students.add(userSetUp.createStudent("student2", user1));
        students.add(userSetUp.createStudent("student3", user1));
        students.add(userSetUp.createStudent("student4", user1));
        students.add(userSetUp.createStudent("student5", user1));
        studentRepository.saveAll(students);

        DrawingDto drawingDto = DrawingDto.builder()
                .name("test1")
                .winnerCount(1)
                .studentIdList(students.stream().map(Student::getId).toList())
                .build();

        //when
        DrawingResponse drawingResponse = drawingService.randomDrawing(drawingDto, user1);

        //then
        assertEquals(1, drawingResponse.getWinnerCount());
        assertEquals(1, drawingResponse.getWinnerList().size());
        assertEquals(4, drawingResponse.getLoserList().size());
    }

    private Drawing createDrawing(String name, User user) {
        return Drawing.builder()
                .name(name)
                .user(user)
                .winnerCount(1)
                .build();
    }
}