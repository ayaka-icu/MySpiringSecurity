package com.ganga;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class EncodeApplicationTests {

    @Test
    void BCryptPasswordEncoderTest() {

        BCryptPasswordEncoder encoderUtils = new BCryptPasswordEncoder();
        for (int i = 0; i < 3; i++) {
            System.out.println(encoderUtils.encode("123"));
        }
        //$2a$10$8wQ/3DZTUqQpxCv1kJolL.2LiYQx1FtvGccV5D9ayxou6LS6cfFwG
        //$2a$10$QwfEUEw9pHj4pNU9/4m0WuxaJc2cneMkmGfqHvaHyR64WXKp/dlQy
        //$2a$10$HJzyDm34f9yH8wFsy5.3GeCgb8.XgsOkHIBoBoGYHiZFFzElfGOai

        System.out.println("=================================================");

        BCryptPasswordEncoder encoderBC = new BCryptPasswordEncoder(16);
        for (int i = 0; i < 3; i++) {
            System.out.println(encoderBC.encode("123"));
        }
        //$2a$16$cD4Lqb.e4CKaxkQSqVHVSOx4hVpnzLshqDifnSty.8LtHCMqTRFia
        //$2a$16$zGX2c2sEXQ8OZqDI5uR.xOmOI6GyW3oLW1WfrHQFyO0VerSzwD.L2
        //$2a$16$CtOKI1oHle1RJVY1ZY5SQu6V93F9S0OcbbfyZ2ln5duO98SwMdlxS

    }

    @Test
    public void meTest() {
        BCryptPasswordEncoder encoderBCrypt = new BCryptPasswordEncoder(16);
        boolean matches = encoderBCrypt.matches("123", "$2a$16$cD4Lqb.e4CKaxkQSqVHVSOx4hVpnzLshqDifnSty.8LtHCMqTRFia");
        System.out.println(matches);
    }

}
