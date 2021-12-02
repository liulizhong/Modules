package mysql.druid工具类;

/*
 * @class Bean的员工类，和数据库字段对应
 */
public class Employee {
    private int id;
    private String name;
    private Double grade;

    public Employee(int id, String name, Double grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public Employee() {
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name='" + name + '\'' + ", grade=" + grade + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
