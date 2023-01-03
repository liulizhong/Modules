package aaa课程代码.day10_封装继承多态.练习题2_继承;

public class Manager extends Employee {
	private double bonus;

	public Manager(String name, double salary, double bonus) {
		super(name, salary);//���ø�����вι���
		this.bonus = bonus;
	}

	public Manager() {
		super();//���ø�����޲ι��죬��ȫ����ʡ�ԣ���д��д����������
	}

	public double getBonus() {
		return bonus;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}

	public String getInfo(){
		//return "������" + name + "��н�ʣ�" + salary + "������" + bonus;//����ģ�����˽�е����ԣ��������в��ɼ�
		return "������" + getName() + "��н�ʣ�" + getSalary() + "������" + bonus;
	}
}
