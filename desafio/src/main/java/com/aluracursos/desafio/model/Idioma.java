package com.aluracursos.desafio.model;

public enum Idioma {
    ES ("es", "español"),
    EN ("en", "ingles"),
    FR ("fr", "frances"),
    PT ("pt", "portugues");

    private String idiomaOmdb;
    private String idiomaEspanol;

    Idioma(String idiomaOmdb, String idiomaEspanol) {
        this.idiomaOmdb = idiomaOmdb;
        this.idiomaEspanol = idiomaEspanol;
    }

    public static Idioma fromString(String text){
        for (Idioma idioma : Idioma.values()){
            if (idioma.idiomaOmdb.equalsIgnoreCase(text)){
                return idioma;
            }
        }
        throw new IllegalArgumentException("Ningun idioma encontrado: " + text);
    }

    public static Idioma fromEspanol(String text){
        for (Idioma idioma: Idioma.values()){
            if (idioma.idiomaEspanol.equalsIgnoreCase(text)){
                return idioma;
            }
        }
        throw new IllegalArgumentException("Ningun idioma encontrado: " + text);
    }
}
