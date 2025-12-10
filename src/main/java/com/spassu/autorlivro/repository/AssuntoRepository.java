package com.spassu.autorlivro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spassu.autorlivro.model.Assunto;

@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Long> {
    
    // Optional<Assunto> findByDescricao(String descricao);
}
