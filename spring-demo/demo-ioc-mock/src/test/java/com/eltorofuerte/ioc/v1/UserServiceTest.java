package com.eltorofuerte.ioc.v1;

import com.eltorofuerte.ioc.v1.service.UserService;
import org.junit.Test;

public class UserServiceTest {

    @Test
    public void learn() {
        new UserService().learn();
    }
}