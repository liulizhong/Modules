package aaa课程代码.day11_项目2家庭收支管理.view;

import aaa课程代码.day11_项目2家庭收支管理.bean.Customer;
import aaa课程代码.day11_项目2家庭收支管理.service.CustomerService;
import aaa课程代码.day11_项目2家庭收支管理.util.CMUtility;

public class CustomerView {
	CustomerService cs = new CustomerService(2);

	public void menu(){
		while(true){
			System.out.println("-----------------客户信息管理软件-----------------");
			System.out.println("\t\t1 添 加 客 户");
			System.out.println("\t\t2 修 改 客 户");
			System.out.println("\t\t3 删 除 客 户");
			System.out.println("\t\t4 客 户 列 表");
			System.out.println("\t\t5 退           出");
			System.out.print("\t\t请选择(1-5)：");

			//因为被调用的方法是static，可以用类名.调用
			char select = CMUtility.readMenuSelection();
			switch(select){
				case '1':
					add();
					break;
				case '2':
					update();
					break;
				case '3':
					delete();
					break;
				case '4':
					list();
					break;
				case '5':
					System.out.println("是否退出(Y/N)?");
					char confirm = CMUtility.readConfirmSelection();
					if(confirm == 'Y'){
						return;
					}
			}
		}
	}

	private void list() {
		System.out.println("---------------------------客户列表---------------------------");
		System.out.println("编号\t姓名\t性别\t年龄\t电话\t 邮箱");

		//1、查询所有客户对象

		Customer[] all = cs.getAll();

		//2、遍历数组
		for (int i = 0; i < all.length; i++) {
			System.out.println((i+1) + "\t" + all[i].getInfo());
		}
		System.out.println("-------------------------客户列表完成-------------------------");
	}

	private void delete() {
		System.out.println("---------------------删除客户---------------------");
		//1、输入删除的客户编号
		System.out.print("请选择待删除客户编号(-1退出)：");
		int id = CMUtility.readInt();
		if(id == -1){
			return;
		}

		//2、确认是否删除
		System.out.println("是否删除(Y/N)?");
		char confirm = CMUtility.readConfirmSelection();
		if(confirm == 'N'){
			return;
		}

		//3、真正删除
		boolean flag = cs.deleteById(id);
		System.out.println(flag ? "删除成功" : "删除失败");

		System.out.println("---------------------删除完成---------------------");
	}

	private void update() {
		System.out.println("---------------------修改客户---------------------");
		//1、输入修改的客户编号
		System.out.print("请选择待修改客户编号(-1退出)：");
		int id = CMUtility.readInt();
		if(id == -1){
			return;
		}

		//2、根据编号查询该客户原来的信息
		Customer old = cs.getById(id);
		if(old != null){
			//3、接收用户输入的新数据
			System.out.print("姓名(" + old.getName()+"):");
			String name = CMUtility.readString(20,old.getName());

			System.out.print("性别(" +old.getGender() + ")：");
			char gender = CMUtility.readChar(old.getGender());

			System.out.print("年龄(" +old.getAge() + ")：");
			int age = CMUtility.readInt(old.getAge());

			System.out.print("电话(" +old.getPhone() + ")：");
			String phone = CMUtility.readString(11, old.getPhone());

			System.out.print("邮箱(" +old.getEmail() + ")：");
			String email = CMUtility.readString(32, old.getEmail());

//			old.setName(name);
//			old.setGender(gender);
//			old.setAge(age);
//			old.setPhone(phone);
//			old.setEmail(email);

			//4、重新封装为一个新的客户对象
			Customer newCustomer = new Customer(name, gender, age, phone, email);

			//5、替换数组中原来的对象
			boolean flag = cs.replace(id, newCustomer);
			System.out.println(flag ? "修改成功" : "修改失败");
		}
		System.out.println("---------------------修改完成---------------------");
	}

	private void add() {
		System.out.println("---------------------添加客户---------------------");
		//1、接收用户输入
		System.out.print("姓名：");
		String name = CMUtility.readString(20);

		System.out.print("性别：");
		char gender = CMUtility.readChar();

		System.out.print("年龄：");
		int age = CMUtility.readInt();

		System.out.print("电话：");
		String phone = CMUtility.readString(11);

		System.out.print("邮箱：");
		String email = CMUtility.readString(32);

		//2、封装一个客户对象
		Customer customer = new Customer(name, gender, age, phone, email);

		//3、存储数组中
		boolean flag = cs.addCustomer(customer);
		System.out.println(flag?"添加成功":"添加失败");

		System.out.println("---------------------添加完成---------------------");
	}
}
