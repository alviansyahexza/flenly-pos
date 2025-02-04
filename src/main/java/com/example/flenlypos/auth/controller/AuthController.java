package com.example.flenlypos.auth.controller;

import com.example.flenlypos.ApiResponse;
import com.example.flenlypos.auth.model.dto.UserDto;
import com.example.flenlypos.auth.model.form.SignIn;
import com.example.flenlypos.auth.model.form.SignUp;
import com.example.flenlypos.auth.model.form.UserAdd;
import com.example.flenlypos.auth.service.UserService;
import com.example.flenlypos.auth.tools.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @PostMapping("/sign-in")
    public String signIn(@RequestBody SignIn signInReq) {
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(signInReq.getUsername(), signInReq.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (authenticate.isAuthenticated()) {
            UserDto userDto = userService.loadUserByUsername(signInReq.getUsername());
            return jwtUtils.generateToken(userDto);
        }
        throw new UsernameNotFoundException("invalid username/password");
    }

    @PostMapping("/sign-up")
    public ApiResponse<Boolean> signUp(@RequestBody SignUp signUpReq) {
        userService.signUp(signUpReq);
        return ApiResponse.<Boolean>builder().data(true).build();
    }

    @PostMapping("/add")
    public ApiResponse<Boolean> add(@AuthenticationPrincipal UserDto userDto, @RequestBody UserAdd userAdd) {
        userService.add(userAdd, userDto.getStoreId(), userDto.getId());
        return ApiResponse.<Boolean>builder().data(true).build();
    }
}
