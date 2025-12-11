package com.spassu.autorlivro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spassu.autorlivro.model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    // Verifica se já existe um livro com mesmo título e ano
    boolean existsByTituloIgnoreCaseAndAnoPublicacao(String titulo, String anoPublicacao);

}
