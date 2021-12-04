package com.xuexi.springbootswagger.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName User
 * @create 2020-07-24 17:39
 * @Des TODO
 */
@Data
@ApiModel(description = "点表维度信息")
public class User {
    @ApiModelProperty(value = "年龄")
    private int Age;
    @ApiModelProperty(value = "名字")
    private String name;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "密码")
    private String password;

    public User() {
    }

    public User(int age, String name, String email, String password) {
        Age = age;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
