package com.spassu.autorlivro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spassu.autorlivro.model.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    
    // Optional<Autor> findByNome(String nome);
}
