package com.netdisk.controller;

import com.netdisk.entity.User;
import com.netdisk.service.FileService;
import com.netdisk.service.FileStoreService;
import com.netdisk.service.FolderService;
import com.netdisk.service.UserService;
import com.netdisk.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName: BaseController
 * @Description:
 * @Date: 2022/4/29 21:13
 */
public class BaseController {
    @Autowired
    protected UserService userService;

    @Autowired
    protected FileService fileService;
    @Autowired
    protected JavaMailSenderImpl mailSender;

    @Autowired
    protected FolderService folderService;

    @Autowired
    protected FileStoreService fileStoreService;

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected HttpSession session;

    protected User loginUser;

    protected MailUtil mailUtil;

    /**
     * 设置http会话、请求和回复
     * @param request
     * @param response
     */
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession(true);
        loginUser = (User) session.getAttribute("loginUser");
    }

}
