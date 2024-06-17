package birdjun.profairmanager.drawing.controller;

import birdjun.profairmanager.common.UserSetUp;
import birdjun.profairmanager.drawing.domain.Drawing;
import birdjun.profairmanager.drawing.service.DrawingService;
import birdjun.profairmanager.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DrawingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private DrawingService drawingService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("추첨 생성 요청을 하면 생성이 되어야 한다.")
    @WithMockUser(username = "user")
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
        ResultActions resultActions = mockMvc.perform(post("/drawing/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drawing))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("추첨 리스트 요청을 하면 유저의 리스트를 보여줘야 한다")
    @WithMockUser(username = "user")
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("user", user1);

        ResultActions resultActions = mockMvc.perform(get("/drawing/list")
                        .accept(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", equalTo(drawing1.getName())));
    }

    @Test
    @DisplayName("추첨 리스트 요청과 이름을 주면 유저의 리스트와 이름에 맞는 사람을 보여줘야 한다")
    @WithMockUser(username = "user")
    public void givenDrawing_whenCallListNameApi_thenReturnDrawingList() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("user", user1);

        ResultActions resultActions = mockMvc.perform(get("/drawing/list/name")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("name", drawing1.getName())
                        .session(mockHttpSession))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", equalTo(drawing1.getName())));
    }

    @Test
    @DisplayName("추첨 리스트 요청과 이름을 주었을때 유저가 만든 정보가 하나도 없으면 아무것도 나오지 않아야 한다.")
    @WithMockUser(username = "user")
    public void givenNoDrawing_whenCallListNameApi_thenReturnNothing() throws Exception {
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
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("user", user1);

        ResultActions resultActions = mockMvc.perform(get("/drawing/list/name")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("name", drawing1.getName())
                        .session(mockHttpSession))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }
}