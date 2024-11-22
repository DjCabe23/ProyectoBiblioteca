package com.example.damian.ProyectoBiblioteca.Service;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.example.damian.ProyectoBiblioteca.model.Autor;
import com.example.damian.ProyectoBiblioteca.model.AutorRepository;
import com.example.damian.ProyectoBiblioteca.model.Libro;
import com.example.damian.ProyectoBiblioteca.model.LibroRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GutendexClient {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LibroRepository libroRepository;

    private static final String BASE_URL = "https://gutendex.com/books/";

    public String getBooks(String query) throws IOException, InterruptedException {
        String url = BASE_URL + "?search=" + query;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); // Solo devolvemos el JSON bruto por ahora
    }

    public void guardarLibrosDesdeApi(String query) throws IOException, InterruptedException {
        String url = BASE_URL + "?search=" + query;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("HTTP error: " + response.statusCode());
        }

        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray books = jsonResponse.getJSONArray("results");

        for (int i = 0; i < books.length(); i++) {
            JSONObject bookJson = books.getJSONObject(i);

            String title = bookJson.getString("title");
            JSONArray authors = bookJson.getJSONArray("authors");
            JSONArray languages = bookJson.getJSONArray("languages");
            int downloadCount = bookJson.getInt("download_count");

            if (authors.length() > 0) {
                JSONObject authorJson = authors.getJSONObject(0);
                String authorName = authorJson.getString("name");
                int birthYear = authorJson.optInt("birth_year", -1);
                int deathYear = authorJson.optInt("death_year", -1);

                Autor autor = autorRepository.findByNombre(authorName);
                if (autor == null) {
                    autor = new Autor();
                    autor.setNombre(authorName);
                    autor.setAnioNacimiento(birthYear);
                    autor.setAnioFallecimiento(deathYear);
                    autor = autorRepository.save(autor);
                }

                Libro libro = new Libro();
                libro.setTitulo(title);
                libro.setIdiomas(languages.join(", "));
                libro.setNumeroDescargas(downloadCount);
                libro.setAutor(autor);

                libroRepository.save(libro);
            }
        }
    }
}
