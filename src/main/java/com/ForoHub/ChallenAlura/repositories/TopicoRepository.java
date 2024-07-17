package com.ForoHub.ChallenAlura.repositories;

import com.ForoHub.ChallenAlura.models.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    Optional<Topico> findByTituloAndMensaje(String titulo, String mensaje);

    @Query("SELECT t FROM Topico t WHERE t.curso = :curso AND YEAR(t.fechaCreacion) = :year")
    List<Topico> findByCursoAndYear(@Param("curso") String curso, @Param("year") int year);
}
