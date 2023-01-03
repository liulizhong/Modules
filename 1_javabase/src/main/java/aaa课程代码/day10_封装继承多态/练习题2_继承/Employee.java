package aaa课程代码.day10_封装继承多态.练习题2_继承;

public class Employee {
	private String name;
	private double salary;
	public Employee(String name, double salary) {
		this.name = name;
		this.salary = salary;
	}
	public Employee() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public String getInfo(){
		return "������" + name + "��н�ʣ�" + salary;
	}
	
}
