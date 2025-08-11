package com.marcos.dev.zentasks.zen_task_api.users.infra.security;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.InvalidTokenException;
import com.marcos.dev.zentasks.zen_task_api.users.repository.UserRepository;
import com.marcos.dev.zentasks.zen_task_api.users.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> tokenOpt = this.recoveryToken(request);

        if (tokenOpt.isPresent()) {
            String token = tokenOpt.get();
            try {
                String subject = tokenService.validateToken(token);
                logger.debug("Token válido para o usuário: {}", subject);

                userRepository.findByUsername(subject).ifPresentOrElse(
                        user -> {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            logger.debug("Usuário {} autenticado com sucesso", user.getUsername());
                        },
                        () -> logger.warn("Usuário {} não encontrado no banco de dados", subject)
                );

            } catch (InvalidTokenException e) {
                logger.warn("Token inválido: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Token inválido ou expirado\"}");
                response.setContentType("application/json");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


    private Optional<String> recoveryToken(HttpServletRequest request) {
        String authHeaderValue = request.getHeader("Authorization");

        if (authHeaderValue == null) {
            return Optional.empty();
        }

        return Optional.of(authHeaderValue.replace("Bearer ", ""));
    }
}
