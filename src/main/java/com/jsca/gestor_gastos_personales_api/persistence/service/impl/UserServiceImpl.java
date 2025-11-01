package com.jsca.gestor_gastos_personales_api.persistence.service.impl;

import com.jsca.gestor_gastos_personales_api.persistence.dto.request.RegisterRequest;
import com.jsca.gestor_gastos_personales_api.persistence.dto.response.UserResponse;
import com.jsca.gestor_gastos_personales_api.persistence.entities.UserEntity;
import com.jsca.gestor_gastos_personales_api.persistence.repository.UserRepository;
import com.jsca.gestor_gastos_personales_api.persistence.service.UserService;
import com.jsca.gestor_gastos_personales_api.util.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registrar nuevo usuario
     */
    @Override
    @Transactional
    public UserResponse registerUser(final RegisterRequest request) throws Exception {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new Exception("El username  ya esta en uso");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception("Ya existe una cuenta con este email");
        }
        UserEntity user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    /**
     * Buscar usuario por ID
     */
    @Override
    public UserResponse getUserById(final Long userId) throws Exception {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Ocurrio un error"));

        return userMapper.toResponse(user);
    }

    /**
     * Buscar usuario por username
     */
    @Override
    public UserResponse getUserByUsername(final String username) throws Exception {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("No existe este usuario"));

        return userMapper.toResponse(user);
    }

    /**
     * Buscar entidad de usuario
     */
    @Override
    public UserEntity findUserEntityById(final Long userId) throws Exception {
        return userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Error"));
    }

    /**
     * Buscar entidad por username
     */
    @Override
    public UserEntity findUserEntityByUsername(final String username) throws Exception {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
    }

    /**
     * Buscar usuario por identificacion
     */
    @Override
    public UserEntity findUserEntityByIdentification(final String identification) throws Exception {
        return userRepository.findByIdentification(identification)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
    }
}
