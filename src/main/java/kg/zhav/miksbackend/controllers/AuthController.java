package kg.zhav.miksbackend.controllers;


import kg.zhav.miksbackend.dto.AuthLoginDto;
import kg.zhav.miksbackend.dto.CustomUserDetails;
import kg.zhav.miksbackend.dto.JwtTokenDto;
import kg.zhav.miksbackend.dto.UserDto;
import kg.zhav.miksbackend.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("login")
    public JwtTokenDto login(@RequestBody AuthLoginDto loginDto){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        if (authenticate.isAuthenticated()){
            String token = jwtService.GenerateToken(loginDto.getUsername());
            return new JwtTokenDto(token);
        }
        throw new BadCredentialsException("Invalid username or password");
    }

    @GetMapping
    public Object getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails userDetail) {
            return new UserDto(userDetail.user());
        }
        return principal;
    }
}
