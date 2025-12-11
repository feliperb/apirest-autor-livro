package com.spassu.autorlivro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spassu.autorlivro.model.Assunto;

@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Long> {

    // Verifica se já existe um assunto com a mesma descrição (ignora maiúsculas/minúsculas)
    boolean existsByDescricaoIgnoreCase(String descricao);

    // Verifica se já existe outro assunto com a mesma descrição, ignorando um ID específico
    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);
}
