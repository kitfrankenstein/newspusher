package com.kitfrankenstein.newspusher.controller;

import com.alibaba.fastjson.JSONObject;
import com.kitfrankenstein.newspusher.entity.User;
import com.kitfrankenstein.newspusher.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Kit
 * @date: 2019/8/1 18:25
 */
@Controller
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public JSONObject login(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String phoneNumber = jsonObject.getString("phoneNumber");
        String password = jsonObject.getString("password");
        User user = new User().setPhoneNumber(phoneNumber).setPassword(password);
        int flag = userService.login(user);
        JSONObject response = new JSONObject();
        if (flag == 1) {
            request.getSession().setAttribute("loginUser", user);
            response.put("flag", 1000);
            response.put("msg", "success");
        } else if (flag == 0) {
            response.put("flag", 0);
            response.put("msg", "账号不存在");
        } else {
            response.put("flag", -1);
            response.put("msg", "密码错误");
        }
        return response;
    }

    @PostMapping("/register")
    @ResponseBody
    public JSONObject register(@RequestBody JSONObject jsonObject) {
        String phoneNumber = jsonObject.getString("phoneNumber");
        String password = jsonObject.getString("password");
        String password2 = jsonObject.getString("password2");
        boolean flag = password.equals(password2);
        JSONObject response = new JSONObject();
        if (!flag) {
            response.put("flag", 0);
            response.put("msg", "两次输入的密码不一致");
            return response;
        }
        User user = new User().setPhoneNumber(phoneNumber).setPassword(password);
        if (userService.register(user)) {
            response.put("flag", 1000);
            response.put("msg", "注册成功");
        } else {
            response.put("flag", -1);
            response.put("msg", "注册失败");
        }
        return response;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET) //匹配的是href中的download请求
    public ResponseEntity<byte[]> download() throws IOException {
        String downloadFilePath = "/usr";//上传文件夹
        File file = new File(downloadFilePath + File.separator + "newsPusher.apk");//新建文件
        HttpHeaders headers = new HttpHeaders();//http头信息
        String downloadFileName = new String("newsPusher.apk".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);//设置编码
        headers.setContentDispositionFormData("attachment", downloadFileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //MediaType:互联网媒介类型  contentType：具体请求中的媒体类型信息
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }
}
