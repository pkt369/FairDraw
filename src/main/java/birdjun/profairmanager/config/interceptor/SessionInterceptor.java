package birdjun.profairmanager.config.interceptor;

import birdjun.profairmanager.user.domain.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user != null && modelAndView != null) {
            modelAndView.addObject("userName", user.getName());
        }
    }
}
