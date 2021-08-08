package tobyspring.pjt.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tobyspring.pjt.UserDao;

@Service
@AllArgsConstructor
public class UserService {
    UserDao userDao;


}
