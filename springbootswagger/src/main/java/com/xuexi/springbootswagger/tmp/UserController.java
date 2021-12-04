package com.xuexi.springbootswagger.tmp;

import com.xuexi.springbootswagger.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName UserController
 * @create 2020-07-24 17:49
 * @Des TODO
 */

@Api(tags = "test自动化数据管理")  //接口集的标签题
@RestController
public class UserController {
    @ApiOperation(value = "查询操作", notes = "用户查询操作")
    @GetMapping(value = "/select")
    public User select(@ApiParam(value = "姓名", required = true) @RequestParam String name) {
        User u = new User();
        u.setAge(18);
        u.setEmail("**@163.com");
        u.setPassword("123456");
        u.setName(name);
        return u;
    }

    @ApiOperation(value = "新增操作", notes = "用户新增操作")
    @GetMapping(value = "/add")
    public User add(User user) {
        return user;
    }
}
