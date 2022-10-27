package com.ganga.controller;

import com.ganga.domain.Student;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/me")
public class MethodController {

    @GetMapping
    public String init(){
        return "认证通过";
    }

    //@PreAuthorize 目标方法之前进行权限认证 支持认证表达式
    @PreAuthorize("hasRole('ADMIN') and authentication.name=='ganga'")
    @GetMapping("/test01")
    public String test01(){
        return "test 01 ...";
    }

    //@PreAuthorize 也可以对 权限字符串进行认证
    @PreAuthorize("hasAuthority('READ_BOOK')")
    @GetMapping("/test02")
    public String test02(){
        return "test 02 ...";
    }

    //可以支持 el表达式 【要去掉{}】 --> #参数
    @PreAuthorize("authentication.name==#name")
    @GetMapping("/test03")
    public String test03(String name){
        return "test 03 ... name= "+name;
    }

    //@PreFilter 对目标方法执行前 对参数进行过滤授权  || Test Json：
    //value: 指定过滤规则 filterObject 过滤的对象   || [{"uid":1,"username":"stu1"},{"uid":2,"username":"stu2"},{"uid":3,"username":"stu3"},
    //filterTarget: 过滤目标 必须是 数组 集合       || {"uid":4,"username":"stu4"},{"uid":5,"username":"stu5"},{"uid":6,"username":"stu6"}]
    @PreFilter(value="filterObject.uid%2!=0",filterTarget = "students")
    @PostMapping("/test04")
    public String test04(@RequestBody List<Student> students){
        return students.toString();
    }

    //@PostAuthorize: 目标方法执行后 返回之前 进行处理
    @PostAuthorize("returnObject.uid%2!=0 and returnObject.username.length()>=3")
    @GetMapping("/test05")
    public Student test05(Integer uid,String username){
        return new Student(username,uid);
    }//http://localhost:8080/me/test05?uid=3&username=ganga

    //@PostFilter: 目标方法执行之后 返回之前 进行对参数的过滤 参数是个集合 或 数组
    //filterObject 是返回的数组 或 集合 的 每一个存储对象 进行过滤
    @PostFilter("filterObject.uid%2!=0")
    @GetMapping("/test06")
    public List<Student> test06(){
        ArrayList<Student> students = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            students.add(new Student("stu"+1,i));
        }
        return students;
    }//http://localhost:8080/me/test06


    //@Secured 进行角色认证 不支持表达式  里面的参数是所需要的角色 满足其中一个即可
    //可以加上ROLE_ 也可以省略ROLE_
    @Secured({"ROLE_ADMIN","USER"})
    @GetMapping("secured")
    public String secured(){
        return "me ... secured";
    }



    //JSR250
    @DenyAll
    @GetMapping("/jsr250DenyAll")
    public String jsr250DenyAll(){
        return "访问不到...";
    }

    @PermitAll
    @GetMapping("/jsr250PermitAll")
    public String jsrPermitAll(){
        return "认证后，都可以访问";
    }

    @RolesAllowed({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/jsr250RolesAllowed")
    public String jsr250RolesAllowed(){
        return "jsr250RolesAllowed running...";
    }








}
