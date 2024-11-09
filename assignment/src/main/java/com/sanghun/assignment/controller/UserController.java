package com.sanghun.assignment.controller;

import com.sanghun.assignment.domain.dto.loginDto;
import com.sanghun.assignment.domain.dto.signupDto;
import com.sanghun.assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public signupDto.Response signup(@RequestBody signupDto.Request requestDto){
        return userService.signup(requestDto);
    }

    @PostMapping("/sign")
    public loginDto.Response login(@RequestBody loginDto.Request requestDto){
        return userService.login(requestDto);
    }

}
