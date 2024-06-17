package birdjun.profairmanager.drawing.service;

import birdjun.profairmanager.common.UserSetUp;
import birdjun.profairmanager.drawing.domain.Drawing;
import birdjun.profairmanager.drawing.repository.DrawingRepository;
import birdjun.profairmanager.user.domain.User;
import birdjun.profairmanager.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

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
    private UserSetUp userSetUp;


    @BeforeEach
    void setUp() {
        drawingService = new DrawingService(drawingRepository);
        userSetUp = new UserSetUp(userRepository);
    }


    @Test
    @DisplayName("추첨 생성 요청을 하면 생성이 되어야 한다.")
    public void givenDrawing_whenCallCreateApi_thenSuccess() throws Exception {
        //given
        User user = userSetUp.createUser("user1");
        userSetUp.save(user);

        Drawing drawing = Drawing.builder()
                .name("drawing1")
                .user(user)
                .isDuplicated(Boolean.FALSE)
                .winnerCount(1)
                .build();

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

        Drawing drawing1 = Drawing.builder()
                .name("drawing1")
                .user(user1)
                .isDuplicated(Boolean.FALSE)
                .winnerCount(1)
                .build();
        drawingService.save(drawing1);

        Drawing drawing2 = Drawing.builder()
                .name("drawing2")
                .user(user2)
                .isDuplicated(Boolean.FALSE)
                .winnerCount(2)
                .build();
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

        Drawing drawing1 = Drawing.builder()
                .name("drawing1")
                .user(user1)
                .isDuplicated(Boolean.FALSE)
                .winnerCount(1)
                .build();
        drawingService.save(drawing1);

        Drawing drawing2 = Drawing.builder()
                .name("drawing2")
                .user(user2)
                .isDuplicated(Boolean.FALSE)
                .winnerCount(2)
                .build();
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

        Drawing drawing1 = Drawing.builder()
                .name("drawing1")
                .user(user2)
                .isDuplicated(Boolean.FALSE)
                .winnerCount(1)
                .build();
        drawingService.save(drawing1);

        Drawing drawing2 = Drawing.builder()
                .name("drawing2")
                .user(user2)
                .isDuplicated(Boolean.FALSE)
                .winnerCount(2)
                .build();
        drawingService.save(drawing2);

        //when
        List<Drawing> list = drawingService.findByNameAndUser(drawing1.getName(), user1, PageRequest.of(0, 10));

        //then
        assertEquals(0, list.size());
    }

}