package tobyspring.pjt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tobyspring.pjt.domain.Level;


import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class UserDaoTest {

    private User user1;
    private User user2;
    private User user3;

    @Mock
    private UserDao dao;

    @BeforeAll
    public void setUp(){
        this.user1 = new User("hi", "하이", "hi01", Level.BASIC, 1, 0);
        this.user2 = new User("hoi", "호이", "hoi01", Level.SILVER, 88, 30);
        this.user3 = new User("hei", "헤이", "hei01", Level.GOLD, 10, 70);
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1, user1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);

    }

    @Test
    public void update() throws SQLException, ClassNotFoundException {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);

        user1.setName("hohoi");

        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);
    }

    private void checkSameUser(User user1, User user2){
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
    }
}
