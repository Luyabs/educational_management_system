package com.example.educational_management_system;

import com.example.educational_management_system.common.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTest {
    @Test
    void jwt() {
        String token = JwtUtils.generateToken("张三");
        System.out.println(token);

        String subject = JwtUtils.decodeByToken(token); //getClaimsByToken(token).getSubject();
        System.out.println(subject);
    }
}
