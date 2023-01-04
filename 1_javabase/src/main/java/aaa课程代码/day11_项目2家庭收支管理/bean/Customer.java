package aaa课程代码.day11_项目2家庭收支管理.bean;


/**
 * String name ：客户姓名
 * char gender  ：性别
 * int age          ：年龄
 * String phone：电话号码
 * String email ：电子邮箱
 */
public class Customer {
    private String name;
    private char gender;
    private int age;
    private String phone;
    private String email;

    public Customer(String name, char gender, int age, String phone, String email) {
        super();
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.email = email;
    }

    public Customer() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInfo() {
        //姓名       性别    年龄   电话            邮箱
        return name + "\t" + gender + "\t" + age + "\t" + phone + "\t" + email;
    }
}
