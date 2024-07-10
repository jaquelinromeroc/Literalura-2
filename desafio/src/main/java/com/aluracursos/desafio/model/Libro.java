package com.aluracursos.desafio.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")

public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Autor> autores = new ArrayList<>();

    public Libro(){

    }

    public Libro(String titulo, Idioma idioma){
        this.titulo = titulo;
        this.idioma = idioma;
    }



    public Libro(DatisLibros datos) {
        this.titulo = datos.titulo();
        if (!datos.idiomas().isEmpty()){
            this.idioma = Idioma.valueOf(datos.idiomas().get(0).toUpperCase());
        }

        if (datos.autor() != null){
            for (DatosAutor datosAutor : datos.autor()){
                Autor autor = new Autor(datosAutor.nombre(), datosAutor);
                autor.setLibro(this);
                this.autores.add(autor);
            }
        }
    }

    @Override
    public String toString(){
        return "Libro{" +
                "titulo: " + titulo + '\'' +
                ", idioma: " + idioma +
                '}';
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdiomas(Idioma idiomas) {
        this.idioma = idiomas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutor(List<Autor> autores) {
        this.autores = autores;
    }
}
