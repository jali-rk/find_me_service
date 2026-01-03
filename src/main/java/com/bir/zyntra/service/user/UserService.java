package com.bir.zyntra.service.user;

import com.bir.zyntra.model.UserAccount;
import com.bir.zyntra.model.enums.AuthProvider;
import com.bir.zyntra.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserAccountRepository userRepo;

    public Optional<UserAccount> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Optional<UserAccount> findById(UUID id) { return userRepo.findById(id);}

    public UserAccount findOrCreateUser(AuthProvider provider, String providerUserId, String email, String name, String pictureUrl) {
        return userRepo.findByProviderAndProviderUserId(provider, providerUserId)
                .orElseGet(() -> {
                    log.info("Creating new SSO user: {}", email);

                    UserAccount user = UserAccount.builder()
                            .provider(provider)
                            .providerUserId(providerUserId)
                            .email(email)
                            .name(name)
                            .pictureUrl(pictureUrl)
                            .build();

                    return userRepo.save(user);
                });
    }
}
