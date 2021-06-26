package week1.integer;

import java.sql.SQLException;

public class MainTest {
	// 테스트용 main 메서드 (client)
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		
		UserDao dao = new DaoFactory().userDao();
		
		User user = new User();
		user.setId("whiteship2");
		user.setName("백기선");
		user.setPassword("married");
		
		dao.add(user);
		
		System.out.println(user.getId() + "등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		System.out.println(user2.getId() + " 조회 성공");
	}

}
