package com.ict.healim.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ict.healim.service.LoginService;
import com.ict.healim.vo.MemberVO;
import com.ict.healim.vo.SessionUserVO;

import java.time.Instant;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView("login&join/login");
        return mv;
    }

    @PostMapping("/login_login")
    public ModelAndView getLoginOK(MemberVO lvo, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("home");

        // ��Ű���� ���� Ƚ���� ���� ���� �ð� Ȯ��
        Cookie[] cookies = request.getCookies();
        int loginFailureCount = 0;
        long lockUntil = 0;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("loginFailureCount".equals(cookie.getName())) {
                    loginFailureCount = Integer.parseInt(cookie.getValue());
                }
                if ("lockUntil".equals(cookie.getName())) {
                    lockUntil = Long.parseLong(cookie.getValue());
                }
            }
        }

        // �α��� ���� �������� Ȯ��
        long currentTime = Instant.now().toEpochMilli();
        if (lockUntil > currentTime) {
            mv.setViewName("login&join/login");
            mv.addObject("error", "로그인 실패 횟수 초과로 5분 후 다시 시도하세요.");
            return mv;
        }

        try {
            // �α��� ����
            MemberVO loginVO = loginService.LoginChk(lvo);
            int result = loginService.updateLastLoginTime(lvo);
            if (loginVO != null && passwordEncoder.matches(lvo.getPassword(), loginVO.getPassword())) {
                // �α��� ���� �� ��Ű �� ���� ����
                resetLoginFailureCookies(response); // �α��� ���� �� ���� Ƚ�� �ʱ�ȭ
                setSessionUser(request, loginVO); // ���ǿ� ����� ���� ����

                // "���̵� ���" ��� ó��
                if ("on".equals(request.getParameter("remember"))) {
                    setCookie(response, "rememberedUserId", loginVO.getMber_id(), 60 * 60 * 24 * 15); // 15��
                } else {
                    setCookie(response, "rememberedUserId", null, 0); // ��Ű ����
                }

                mv.setViewName("home");
                return mv;
            } else {
                // �α��� ���� �� ���� Ƚ�� ����

                loginFailureCount++;
                System.out.println("loginFailureCount : " + loginFailureCount);
                if (loginFailureCount >= 5) {

                    lockUntil = currentTime + 5 * 60 * 1000; // 5�� �� ���� ����
                    setCookie(response, "lockUntil", String.valueOf(lockUntil), 5 * 60);
                    mv.addObject("error", "로그인을 " + loginFailureCount + "회 실패 하였으며, 5회 실패시 5분간 차단됩니다.");
                } else {
                	mv.setViewName("login&join/login");
                    mv.addObject("error", "로그인을 " + loginFailureCount + "회 실패 하였으며, 5회 실패시 5분간 차단됩니다.");
                    // error�� ��� ������ ���ִ��� Ȯ��
                    System.out.println("Error message: " + mv.getModel().get("error"));
                }
                setCookie(response, "loginFailureCount", String.valueOf(loginFailureCount), 5 * 60);
                mv.setViewName("login&join/login");
                return mv;
            }
        } catch (Exception e) {
            mv.setViewName("login&join/login");
            mv.addObject("error", "오류가 발생하였습니다.");
            return mv;
        }
    }

    // ��Ű ���� �޼���
    private void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/"); // ��� ��ο��� ���� ����
        response.addCookie(cookie);
    }

    // �α��� ���� ���� ��Ű �ʱ�ȭ
    private void resetLoginFailureCookies(HttpServletResponse response) {
        setCookie(response, "loginFailureCount", "0", 0);
        setCookie(response, "lockUntil", "0", 0);
    }

    // ���ǿ� ����� ���� ����
    private void setSessionUser(HttpServletRequest request, MemberVO loginVO) {
        SessionUserVO sessionUser = new SessionUserVO();
        sessionUser.setUser_id(loginVO.getMber_id());
        sessionUser.setUser_level(loginVO.getMber_level());
        request.getSession().setAttribute("sessionUser", sessionUser);
    }

    // �α׾ƿ� ó��
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/home?logout";
    }
}