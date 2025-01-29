package com.example.flenlypos.auth.controller;

import com.example.flenlypos.auth.model.form.SignIn;
import com.example.flenlypos.auth.tools.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/sign-in")
    public String signIn(@RequestBody SignIn signInReq) {
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(signInReq.getUsername(), signInReq.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (authenticate.isAuthenticated()) {
            return jwtUtils.generateToken(signInReq.getUsername());
        }
        throw new UsernameNotFoundException("invalid username/password");
    }
}
