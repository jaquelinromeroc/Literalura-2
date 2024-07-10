package com.aluracursos.desafio.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatisLibrosResponse {
    private List<DatisLibros> results;

    public List<DatisLibros> getResults() {
        return results;
    }

    public void setResults(List<DatisLibros> results) {
        this.results = results;
    }
}
