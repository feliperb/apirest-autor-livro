-- DROP TABLES (Limpa Tudo)
DROP TABLE IF EXISTS livro_autor CASCADE;
DROP TABLE IF EXISTS livro_assunto CASCADE;
DROP TABLE IF EXISTS autor CASCADE;
DROP TABLE IF EXISTS livro CASCADE;
DROP TABLE IF EXISTS assunto CASCADE;


-- DROP SEQUENCES (Resetar os IDs)
DROP SEQUENCE IF EXISTS autor_id_seq CASCADE;
DROP SEQUENCE IF EXISTS livro_id_seq CASCADE;
DROP SEQUENCE IF EXISTS assunto_id_seq CASCADE;


-- CRIAÇÃO DAS TABELAS
CREATE TABLE autor (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE livro (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(40) NOT NULL,
    editora VARCHAR(40) NOT NULL,
    edicao INTEGER NOT NULL,
    ano_publicacao VARCHAR(4) NOT NULL
);

CREATE TABLE assunto (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(20) NOT NULL
);

CREATE TABLE livro_autor (
    id_livro INTEGER NOT NULL,
    id_autor INTEGER NOT NULL,
    PRIMARY KEY (id_livro, id_autor),
    CONSTRAINT fk_livroautor_livro FOREIGN KEY (id_livro) REFERENCES livro(id),
    CONSTRAINT fk_livroautor_autor FOREIGN KEY (id_autor) REFERENCES autor(id)
);

CREATE TABLE livro_assunto (
    id_livro INTEGER NOT NULL,
    id_assunto INTEGER NOT NULL,
    PRIMARY KEY (id_livro, id_assunto),
    CONSTRAINT fk_livroassunto_livro FOREIGN KEY (id_livro) REFERENCES livro(id),
    CONSTRAINT fk_livroassunto_assunto FOREIGN KEY (id_assunto) REFERENCES assunto(id)
);


-- VIEW DE RELATORIO
CREATE OR REPLACE VIEW vw_relatorio_geral AS
SELECT
    a.id AS autor_id,
    a.nome AS autor_nome,

    l.id AS livro_id,
    l.titulo AS livro_titulo,
    l.editora AS livro_editora,
    l.edicao AS livro_edicao,
    l.ano_publicacao AS livro_ano_publicacao,

    s.id AS assunto_id,
    s.descricao AS assunto_descricao

FROM autor a
JOIN livro_autor la ON la.id_autor = a.id
JOIN livro l ON l.id = la.id_livro
LEFT JOIN livro_assunto ls ON ls.id_livro = l.id
LEFT JOIN assunto s ON s.id = ls.id_assunto

ORDER BY a.nome, l.titulo, s.descricao;


