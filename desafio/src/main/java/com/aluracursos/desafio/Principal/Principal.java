package com.aluracursos.desafio.Principal;

import com.aluracursos.desafio.Repository.AutorRepository;
import com.aluracursos.desafio.Repository.LibroRepository;
import com.aluracursos.desafio.model.*;
import com.aluracursos.desafio.service.ConsumoAPI;
import com.aluracursos.desafio.service.ConvierteDatos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Component
public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private final LibroRepository repositorio;
    private final AutorRepository autorRepository;
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private Scanner teclado = new Scanner(System.in);
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro> libros;

    @Autowired
    public Principal(LibroRepository repositorio, AutorRepository autorRepository, ConsumoAPI consumoAPI) {
        this.repositorio = repositorio;
        this.autorRepository = autorRepository;
        this.consumoAPI = consumoAPI;
        this.teclado = new Scanner(System.in);
        this.conversor = new ConvierteDatos();
    }


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ****************************************
                    1 - Buscar libro 
                    2 - Mostrar libros buscadas
                    3 - Mostar los autores registrados
                    4 - Mostrar autores vivos en un determinado año
                    5 - Mostrar libros por idioma
                                        
                    0-Salir
                    ****************************************
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroWeb();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    mostrarAutoresVivosEnAno();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando aplicacion...");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        }

    }

    private String formatLibro(Libro libro) {
        StringBuilder sb = new StringBuilder();
        sb.append("DatisLibros[titulo=").append(libro.getTitulo()).append(", ");
        sb.append("autor=[");

        for (Autor autor : libro.getAutores()) {
            sb.append("DatosAutor[nombre=").append(autor.getNombre())
                    .append(", fechaDeNacimiento=").append(autor.getFechaDeNacimiento())
                    .append(", fechaDeFallecimiento=").append(autor.getFechaDeFallecimiento())
                    .append("], ");
        }

        if (!libro.getAutores().isEmpty()) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("], idiomas=[");
        if (libro.getIdioma() != null) {
            sb.append(libro.getIdioma().name().toLowerCase());
        } else {
            sb.append("desconocido");
        }
        sb.append("]]");
        return sb.toString();
    }

    private DatisLibros getDatisLibros() {
        System.out.println("Ingresa el nombre completo del libro que deseas buscar: ");
        String tituloLibro = teclado.nextLine();
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        if (datosBusqueda != null && datosBusqueda.libros() != null && !datosBusqueda.libros().isEmpty()) {
            return datosBusqueda.libros().stream()
                    .filter(libro -> libro.titulo().equalsIgnoreCase(tituloLibro))
                    .findFirst()
                    .orElse(null);
        } else {
            System.out.println("No se encontraron datos del libro que proporcionaste, revisa el nombre del titulo que ingresaste");
            return null;
        }
    }

    private void buscarLibroWeb() {
        DatisLibros datos = getDatisLibros();

        if (datos == null) {
            System.out.println("No se encintraron datos del libro que proporcionaste");
            return;
        }
        Optional<Libro> libroExistente = repositorio.findByTituloIgnoreCase(datos.titulo());

        if (libroExistente.isEmpty()) {
            Libro libro = new Libro(datos);
            repositorio.save(libro);
            System.out.println(" Libro guardado en la base de datos: " + formatLibro(libro));
        } else {
            System.out.println("Libro ya guardado anteriormente " + formatLibro(libroExistente.get()));
        }
    }


    private void mostrarLibrosBuscados() {
        List<Libro> libros = repositorio.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros guardados en la base de datos.");
        } else {
            libros.stream()
                    .sorted(Comparator.comparing(libro ->
                            libro.getIdioma() != null ? libro.getIdioma().name() : ""
                    ))
                    .forEach(libro -> System.out.println(formatLibro(libro)));
        }
    }

    private void mostrarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
        } else {
            Map<Autor, List<Libro>> librosPorAutor = new LinkedHashMap<>();

            for (Libro libro : repositorio.findAll()) {
                for (Autor autor : libro.getAutores()) {
                    librosPorAutor.computeIfAbsent(autor, k -> new ArrayList<>()).add(libro);
                }
            }

            for (Map.Entry<Autor, List<Libro>> entry : librosPorAutor.entrySet()) {
                Autor autor = entry.getKey();
                List<Libro> libros = entry.getValue();

                System.out.println("Autor: " + autor);
                System.out.println("Libros:");
                for (Libro libro : libros) {
                    System.out.println("\t" + libro.getTitulo());
                }
                System.out.println();
            }
        }

    }

    private void mostrarAutoresVivosEnAno(){
        System.out.println("Ingresa el año para verificar los autores vivos: ");
        int ano = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autores = autorRepository.findAll();

        List<Autor> autoresVivos = autores.stream()
                .filter(autor -> {
                    Integer nacimiento = autor.getFechaDeNacimiento();
                    Integer fallecimiento = autor.getFechaDeFallecimiento();
                    return nacimiento <= ano && (fallecimiento == null || ano <= fallecimiento);
                })
                .collect(Collectors.toList());

        if (autoresVivos.isEmpty()){
            System.out.println("No hay autores vivos en el año " + ano);
        } else {
            System.out.println("Autores vivos en el año " + ano + ":");
            autoresVivos.forEach(System.out::println);
        }
    }

    private void mostrarLibrosPorIdioma() {
        System.out.println("Ingresa el idioma para buscar los libros (es - español, en - inglés, fr - francés, pt - portugués): ");
        String idiomaInput = teclado.nextLine().trim();

        Idioma idioma;
        try {
            idioma = Idioma.valueOf(idiomaInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma no válido.");
            return;
        }

        List<Libro> librosPorIdioma = repositorio.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No hay libros en " + idioma.name().toLowerCase() + " guardados en la base de datos.");
        } else {
            librosPorIdioma.forEach(System.out::println);
        }
    }
}


