package com.aluracursos.desafio.model;

import jakarta.persistence.*;
import jdk.jfr.Enabled;

import java.util.Objects;

@Entity
@Table (name = "autor")

public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;
    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;

    public Autor(){

    }

    public Autor(String nombre, DatosAutor d){
        this.nombre = nombre;
        this.fechaDeNacimiento = convertirAFechaInteger(d.fechaDeNacimiento());
        this.fechaDeFallecimiento = convertirAFechaInteger(d.fechaDeFallecimiento());
    }

    private Integer convertirAFechaInteger(String fechaString) {
        if (fechaString == null || fechaString.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(fechaString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Autor(String nombre, Integer fechaDeNacimiento , Integer fechaDeFallecimiento) {
        this.nombre = nombre;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }


    @Override
    public String toString(){
        return
                "Autor{" +
                        "nombre: " + nombre + '\'' +
                        "Fecha de Nacimiento:  " + fechaDeNacimiento + '\'' +
                        "Fecha de Fallecimiento: " + fechaDeFallecimiento +
                        '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return Objects.equals(nombre, autor.nombre) &&
                Objects.equals(fechaDeNacimiento, autor.fechaDeNacimiento) &&
                Objects.equals(fechaDeFallecimiento, autor.fechaDeFallecimiento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, fechaDeNacimiento, fechaDeFallecimiento);
    }
}


