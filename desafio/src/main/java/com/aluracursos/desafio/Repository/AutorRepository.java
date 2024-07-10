package com.aluracursos.desafio.Repository;

import com.aluracursos.desafio.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}
