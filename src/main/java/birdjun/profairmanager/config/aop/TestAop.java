package birdjun.profairmanager.config.aop;

import birdjun.profairmanager.user.domain.Role;
import birdjun.profairmanager.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TestAop {

    private final HttpServletRequest request;

    @Before("execution(* birdjun.profairmanager.user.controller.StudentController.*(..)) || execution(* birdjun.profairmanager.drawing.controller.DrawingController.*(..))")
    public void studentAndDrawingAddSession() {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            session.setAttribute("user", new User(1L, "박세준", "pkt0758@gmail.com", Role.USER, "https://lh3.googleusercontent.com/a/ACg8ocIRmFunt9aqbLy3zeHFjmtSaoRfEKeuPpxX4jicLSxYqqaY_b4=s96-c"));
        }
    }
}
