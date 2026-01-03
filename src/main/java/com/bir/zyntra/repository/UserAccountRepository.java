package com.bir.zyntra.repository;

import com.bir.zyntra.model.UserAccount;
import com.bir.zyntra.model.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId);

    boolean existsByEmail(String email);
}
