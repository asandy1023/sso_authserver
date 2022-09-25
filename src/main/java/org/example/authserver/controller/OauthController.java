package org.example.authserver.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author JQ
 */
@Controller
@SessionAttributes("authorizationRequest")
public class OauthController {

    /**
     * 自定義登錄頁面
     *
     * @return
     */
    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    /**
     * 自定義登錄失敗提示界面
     *
     * @param model
     * @return
     */
    @GetMapping("/login-error")
    public ModelAndView loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登陸失敗,帳號或者密碼錯誤！");
        return new ModelAndView("login", "userModel", model);
    }

    /**
     * 自定義用戶授權頁面
     *
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView("authorize");
        view.addObject("clientId", authorizationRequest.getClientId());
        view.addObject("scope", authorizationRequest.getScope().iterator().next());
        return view;
    }
}
