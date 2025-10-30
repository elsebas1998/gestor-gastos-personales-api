package com.jsca.gestor_gastos_personales_api.persistence.repository;

import com.jsca.gestor_gastos_personales_api.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Buscar usuario por username
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Buscar usuario por email
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Meotod para buscar usuario por identificacion
     */
    Optional<UserEntity> findByIdentification(String identification);

    /**
     * Verificar si existe usuario con username
     */
    boolean existsByUsername(String username);

    /**
     * Verificar si existe usuario con email
     */
    boolean existsByEmail(String email);

    /**
     * Buscar usuario por username y que esté activo
     */
    Optional<UserEntity> findByUsernameAndIsActiveTrue(String username);

    /**
     * Útil para queries más complejas
     */
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND u.isActive = true")
    Optional<UserEntity> findActiveUserByEmail(@Param("email") String email);
}
