package week1.integer;

/**
 * userDao에서 사용할 connectionmaker를 설정해 주는 역할 수행
 * IoC개념을 적용: UserDao가 팩토리에 의해 수동적으로 만들어지고,
 * 팩토리가 구현체를 생성하는 책임을 맡는다.
 * 애플리케이션 코드인 UserDao가 팩토리에 의해 사용되기 때문에 팩토리는 작은 프레임워크이다.
 * @author Jungsoo
 *
 */

@Configuration // 애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정정보라는 표시
public class DaoFactory {
	
	@Bean // 오브젝트 생성을 담당하는 IoC용 메서드라는 표시
	public UserDao userDao() {
		UserDao dao = new UserDao(connectionMaker()); // UserDao가 사용할 connectionMaker 구현 클래스를 결정하고 두 객체 사이의 의존관계를 설정한다.
		
		return dao;
	}
	
	// 메서드 분리
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
