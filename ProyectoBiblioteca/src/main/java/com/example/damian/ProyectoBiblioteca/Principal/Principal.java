package com.example.damian.ProyectoBiblioteca.Principal;

import com.example.damian.ProyectoBiblioteca.Service.ConvierteDatos;
import com.example.damian.ProyectoBiblioteca.Service.GutendexClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
@Component
public class Principal implements CommandLineRunner {

    @Autowired
    private GutendexClient gutendexClient;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String option;

        do {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Buscar libro por nombre");
            System.out.println("2. Ver libros buscados");
            System.out.println("3. Salir");
            System.out.println("4. Buscar y guardar libros en la base de datos");
            System.out.print("Elige una opción: ");
            option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.print("Introduce el nombre del libro: ");
                    String query = scanner.nextLine();
                    try {
                        System.out.println(gutendexClient.getBooks(query));
                    } catch (Exception e) {
                        System.err.println("Error al buscar libros: " + e.getMessage());
                    }
                    break;

                case "2":
                    System.out.println("Esta opción aún no está implementada.");
                    break;

                case "4":
                    System.out.print("Introduce el nombre del libro para guardar en la base de datos: ");
                    String queryDb = scanner.nextLine();
                    try {
                        gutendexClient.guardarLibrosDesdeApi(queryDb);
                        System.out.println("Libros guardados en la base de datos.");
                    } catch (Exception e) {
                        System.err.println("Error al guardar los libros: " + e.getMessage());
                    }
                    break;

                case "3":
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        } while (!option.equals("3"));
    }
}