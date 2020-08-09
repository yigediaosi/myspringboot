package com.net.yoga.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.net.yoga.entity.User;
import com.net.yoga.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author gaort
 * @since 2020/8/2 16:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        Page<User> mpUserPage = new Page<>(1,2);
        List<User> iPage = userService.findAllUser();
        iPage.forEach(System.out::println);
    }
}
