package kg.zhav.miksbackend.services;

import org.junit.jupiter.api.Test;

class JwtServiceTest {

    JwtService jwtService = new JwtService();

    @Test
    void generateToken() {
        String user = jwtService.GenerateToken("user");

        System.out.println(user);

    }
}