package com.marcos.dev.zentasks.zen_task_api.users.services;

import com.marcos.dev.zentasks.zen_task_api.users.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Serviço responsável pela autenticação e autorização de usuários.
 * Implementa UserDetailsService para integração com Spring Security.
 */
@Service
public class AuthorizationService implements UserDetailsService {
    private static final Logger log = LogManager.getLogger(AuthorizationService.class);
    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carrega um usuário pelo nome de usuário para autenticação.
     *
     * @param username Nome de usuário para buscar
     * @return Detalhes do usuário encontrado
     * @throws UsernameNotFoundException Se o usuário não for encontrado
     */
    @Override
    @Cacheable("userDetails")
    public UserDetails loadUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            log.warn("Tentativa de autenticação com username vazio");
            throw new UsernameNotFoundException("Invalid credentials");
        }
        
        log.debug("Buscando usuário com username: {}", username);
        
        Optional<UserDetails> userOptional = userRepository.findByUsername(username);
        
        return userOptional.orElseThrow(() -> {
            log.warn("Usuário não encontrado: {}", username);
            return new UsernameNotFoundException("Invalid credentials");
        });
    }
}