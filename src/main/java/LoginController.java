import entity.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;

@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @Autowired
    UserService UserService;

    @RequestMapping("/login")
    public String Login(LoginUser loginUser, HttpServletRequest request){
        LoginUser user = UserService.FindUser_forLogin(loginUser.getPhone_number(),loginUser.getPassword());
        if(user != null){//设置session为后续聊天业务做准备
            HttpSession session = request.getSession();
            session.setAttribute("loginUser",loginUser);
            return "{\"code\":\"200\",\"message\":\"success\"}";
        }else{
            return"{\"code\":500\",\"message\":\"false\"}";
        }
    }
}
