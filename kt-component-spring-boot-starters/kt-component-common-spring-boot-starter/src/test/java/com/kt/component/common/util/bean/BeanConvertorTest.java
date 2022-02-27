package com.kt.component.common.util.bean;

import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class BeanConvertorTest {

    @Test
    public void testCopy() {
        User user = new User();
        user.setName("javis");
        user.setPhone("18586888512");
        User.Info info = new User.Info();
        info.setAge(18);
        user.setInfo(info);
        User copy = BeanConvertor.copy(user, User.class);
        System.out.println(copy);
        assertNotNull(copy);
    }

    @Test
    public void testCopyList() {
        List<User> users = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            users.add(createMockUser(i));
        }
        List<User> usersCopy = BeanConvertor.copyList(users, User.class);
        System.out.println(usersCopy);
        assertNotNull(users);
    }

    private User createMockUser(int i) {
        User user = new User();
        user.setName(i + "：user");
        user.setPhone("18586888512");
        User.Info info = new User.Info();
        info.setAge(18);
        user.setInfo(info);
        return user;
    }

    @Data
    public static class User {
        String name;
        String phone;
        Info info;

        @Data
        private static class Info {
            int age;
        }
    }

}