package com.nbucedog.www.controller;

import com.nbucedog.www.dao.entity.Role;
import com.nbucedog.www.dao.entity.User;
import com.nbucedog.www.service.RoleService;
import com.nbucedog.www.service.UserService;
import com.nbucedog.www.util.ResultTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller(value = "UserController")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    //没有权限，跳转到这里
    @RequestMapping(value = "/noAuth",method = RequestMethod.GET)
    @ResponseBody
    public Map noAuth(){
        return ResultTools.result(403);
    }

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    @ResponseBody
    public Map getUserInfo(String username) {
        try {
            return ResultTools.dataResult(0, userService.findByUsername(username));
        }catch (Exception e) {
            e.printStackTrace();
            return ResultTools.result(1000);
        }
    }

    @RequestMapping(value = "/user",method = RequestMethod.POST)
    @ResponseBody
    public Map postUser(@RequestParam(required = false) MultipartFile file,HttpServletRequest request){
        try {
            String username = request.getParameter("username");
            String sex = request.getParameter("sex");
            if(userService.findByUsername(username)!=null){
                return ResultTools.result(710);
            }
            if(file!=null) {
                byte[] bytes = file.getBytes();
                Path path = Paths.get("avatar/"+username+".jpeg");
                Files.write(path,bytes);
            }else if("female".equals(sex)) {
                Path path = Paths.get("avatar/female.jpg");//没上传头像，使用默认头像
                Path newPath = Paths.get("avatar/"+username+".jpeg");
                Files.copy(path,newPath);
            }
            User user = new User();
            user.setUsername(username);
            String encode = new BCryptPasswordEncoder().encode(request.getParameter("password"));
            user.setPassword(encode);
            user.setNickname(request.getParameter("nickname"));
            user.setSex(request.getParameter("sex"));
            user.setPhone(request.getParameter("phone"));
            user.setMail(request.getParameter("email"));
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleService.findById(4));
            user.setRoleSet(roleSet);
            userService.save(user);
            return ResultTools.result(0);
        }catch (Exception e){
            e.printStackTrace();
            return ResultTools.dataResult(1000,e.getMessage());
        }

    }

    @RequestMapping(value = "/user",method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("#user.id==authentication.principal.id")
    public Map UpdateUser(@RequestBody User user){
        try {
            User oldUser = userService.findById(user.getId());
            oldUser.setNickname(user.getNickname());
            oldUser.setSex(user.getSex());
            oldUser.setPhone(user.getPhone());
            oldUser.setMail(user.getMail());
            oldUser.setResume(user.getResume());
            userService.save(oldUser);
            return ResultTools.result(0);
        }catch (Exception e){
            e.printStackTrace();
            return ResultTools.dataResult(1000,e.getMessage());
        }
    }

    @RequestMapping(value = "/user",method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasAuthority('delete_user')")
    public Map deleteUser(@RequestBody User user){
        try {
            userService.delete(user);
            return ResultTools.result(0);
        }catch (Exception e){
            e.printStackTrace();
            return ResultTools.dataResult(1000,e.getMessage());
        }
    }

    @RequestMapping(value="/avatar/{username:.+}",method = RequestMethod.GET)
    public void getAvatar(@PathVariable("username") String username, HttpServletResponse response){
        FileInputStream fis = null;
//        username = UriUtils.decode(username, StandardCharsets.UTF_8);
        response.setContentType("image/jpeg");
        try{
            OutputStream out = response.getOutputStream();
            File file = new File("avatar/"+username+".jpeg");
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            out.write(b);
            out.flush();
        }catch (FileNotFoundException e){
            try {
                OutputStream out = response.getOutputStream();
                File file = new File("avatar/default.jpeg");
                fis = new FileInputStream(file);
                byte[] b = new byte[fis.available()];
                fis.read(b);
                out.write(b);
                out.flush();
            }catch (Exception er){
                er.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    //从数据库读取，效率太低，放弃
//    @RequestMapping(value="/avatar",method = RequestMethod.GET)
//    public void getAvatar(HttpServletRequest request, HttpServletResponse response){
//        response.setContentType("image/jpeg");
//        try{
//            OutputStream out = response.getOutputStream();
//            User user = userService.findById(1);
//            byte[] b= user.getAvatar().getBytes(1,12310);
//            out.write(b);
//            out.flush();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
