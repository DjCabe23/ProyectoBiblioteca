package com.example.damian.ProyectoBiblioteca.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Puedes agregar consultas personalizadas si es necesario
}