package com.bir.zyntra.controller;

import com.bir.zyntra.model.UserAccount;
import com.bir.zyntra.model.enums.AuthProvider;
import com.bir.zyntra.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sso")
    public UserAccount handleSSOLogin(
            @RequestParam AuthProvider provider,
            @RequestParam String providerUserId,
            @RequestParam String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String pictureUrl
    ) {
        return userService.findOrCreateUser(provider, providerUserId, email, name, pictureUrl);
    }

    @GetMapping("/{email}")
    public UserAccount getUser(@PathVariable String email) {
        return userService.findByEmail(email).orElseThrow();
    }
}
