package com.synrgy.kaboor.backend.security;

import com.synrgy.kaboor.backend.model.Token;
import com.synrgy.kaboor.backend.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        final String jwt = authHeader.substring(7);

        Token storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);

        if (storedToken != null) {
            storedToken.setExpired(true);
            tokenRepository.save(storedToken);
        }
    }

}
