package com.aluracursos.desafio.Repository;

import com.aluracursos.desafio.model.Idioma;
import com.aluracursos.desafio.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autores WHERE l.titulo = :titulo")
    Optional<Libro> findByTituloWithAutores(@Param("titulo") String titulo);

    List<Libro> findByIdioma (Idioma idioma);

    Optional<Libro> findByTituloIgnoreCase(String titulo);
}
