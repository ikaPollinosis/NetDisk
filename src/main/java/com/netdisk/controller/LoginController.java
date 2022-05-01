package com.netdisk.controller;

import com.netdisk.entity.FileStore;
import com.netdisk.entity.User;
import com.netdisk.utils.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName: LoginController
 * @Description: 用户登录控制器
 * @Date: 2022/4/30 15:41
 */
@Controller
public class LoginController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 退出登录，关闭会话
     * @return
     */
    @GetMapping("/logout")
    public String logout() {
        logger.info("用户退出登录");
        session.invalidate();
        return "redirect:/";
    }

    /**
     * 新用户注册
     * @param user
     * @param code
     * @param map
     * @return
     */
    @PostMapping("/register")
    public String register(User user, String code, Map<String, Object> map) {
        String uCode = (String) session.getAttribute(user.getEmail() + "_code");
        if (!code.equals(uCode)) {
            map.put("errorMsg", "验证码错误");
            return "index";
        }
        // 设置用户信息
        user.setUserName(user.getUserName().trim());
        user.setImagePath("/static/img/usericon.png");
        user.setRegisterTime(new Date());
        user.setRole(1);
        if (userService.addUser(user)) {
            FileStore store = FileStore.builder().userId(user.getUserId()).currentSize(0).build();
            fileStoreService.addFileStore(store);
            user.setFileStoreId(store.getFileStoreId());
            userService.update(user);
            logger.info("注册成功，用户为：" + user);
        } else {
            map.put("errorMsg", "注册失败");
            return "index";
        }
        session.removeAttribute(user.getEmail() + "_code");
        session.setAttribute("loginUser", user);
        return "redirect:/index";
    }


    /**
     * 用户登入
     * @param user
     * @param map
     * @return
     */
    @PostMapping("/login")
    public String login(User user, Map<String, Object> map) {
        User userByEmail = userService.queryByEmail(user.getEmail());
        if (userByEmail != null && userByEmail.getPassword().equals(user.getPassword())) {
            session.setAttribute("loginUser", userByEmail);
            logger.info("登录成功" + userByEmail);
            return "redirect:/index";
        } else {
            // 登入失败
            User user1 = userService.queryByEmail(user.getEmail());
            String errorMsg = user1 == null ? "账号尚未注册" : "密码错误";
            logger.info("登录失败");
            map.put("errorMsg", errorMsg);
            return "index";
        }
    }


    /**
     * 向用户邮箱发送验证码
     * @param userName
     * @param email
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendCode")
    public String sendCode(String userName, String email, String password) {
        User userByEmail = userService.queryByEmail(email);
        if (userByEmail != null) {
            logger.error("此邮箱已被注册，请更换其他邮箱");
            return "exitEmail";
        }
        logger.info("发送邮件中：" + mailSender);
        mailUtil = new MailUtil(mailSender);
        String code = mailUtil.sendCaptcha(email, userName, password);
        session.setAttribute(email + "_code", code);
        return "success";
    }

}
