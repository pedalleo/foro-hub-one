package com.ForoHub.ChallenAlura.repositories;

import com.ForoHub.ChallenAlura.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
