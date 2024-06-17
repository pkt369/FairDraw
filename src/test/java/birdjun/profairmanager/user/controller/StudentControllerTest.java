package birdjun.profairmanager.user.controller;

import birdjun.profairmanager.common.UserSetUp;
import birdjun.profairmanager.user.domain.*;
import birdjun.profairmanager.user.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserSetUp userSetUp;


    @Test
    @DisplayName("학생 요청시 생성 시 잘 생성되어야 한다.")
    @WithMockUser(username = "user")
    public void givenCreateApi_whenCreateStudent_thenSuccess() throws Exception {
        //given
        User user = userSetUp.createUser("user1");
        userSetUp.save(user);

        Student student = userSetUp.createStudent("student1");
        student.initUser(user);

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("user", user);

        //when
        ResultActions resultActions = mockMvc.perform(post("/student/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(objectMapper.writeValueAsString(student))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("생성되어 있는 학생들이 있을때 내가 생성한 학생들만 가져와야 한다.")
    @WithMockUser(username = "user")
    public void givenStudent_whenCallListApi_thenSuccess() throws Exception {
        //given
        User user1 = userSetUp.createUser("user1");
        userSetUp.save(user1);

        User user2 = userSetUp.createUser("user2");
        userSetUp.save(user2);

        Student student1 = userSetUp.createStudent("student1");
        student1.initUser(user1);

        Student student2 = userSetUp.createStudent("student2");
        student2.initUser(user2);

        studentService.save(student1);
        studentService.save(student2);

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("user", user1);

        //when
        ResultActions resultActions = mockMvc.perform(get("/student/list")
                        .accept(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", equalTo(student1.getName())));
    }


    @Test
    @DisplayName("생성되어 있는 학생들이 있을때 이름으로 검색하면 내가 생성하고 검색한 학생만 가져와야 한다.")
    @WithMockUser(username = "user", roles = {"USER"})
    public void givenStudent_whenCallListNameApi_thenSuccess() throws Exception {
        //given
        User user1 = userSetUp.createUser("user1");
        userSetUp.save(user1);

        User user2 = userSetUp.createUser("user2");
        userSetUp.save(user2);

        Student student1 = userSetUp.createStudent("student1");
        student1.initUser(user1);

        Student student2 = userSetUp.createStudent("student2");
        student2.initUser(user2);

        studentService.save(student1);
        studentService.save(student2);

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("user", user1);

        //when
        ResultActions resultActions = mockMvc.perform(get("/student/list/name")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("name", student1.getName())
                        .session(mockHttpSession))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", equalTo(student1.getName())));
    }

}

