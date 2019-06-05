package com.gc.webserver.controller;


import com.gc.webserver.util.CodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Controller
public class VerifyCodeController {

    @Autowired
    private CodeUtil codeUtil;

    @RequestMapping(value = "/getVerifyCode/{id}")
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id){
        try {
            response.setContentType("image.jpeg");
            response.setHeader("Pragma","no-cache");
            response.setHeader("Cache-Control","no-cache");
            response.setDateHeader("Expire", 0);
            codeUtil.getRandcode(request,response,id);

        }catch (Exception e){

            log.error("验证码生成错误",e);

        }
    }


}
