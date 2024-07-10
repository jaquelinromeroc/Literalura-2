package com.aluracursos.desafio.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public record DatisLibros(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors")List<DatosAutor> autor,
        @JsonAlias("languages")List<String> idiomas
) {
    @Override
    public String toString() {
        return "DatisLibros{" +
                "titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", idiomas=" + idiomas +
                '}';
    }
}



