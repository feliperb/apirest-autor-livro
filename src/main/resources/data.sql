-- AUTORES
INSERT INTO autor (nome) VALUES 
('Machado de Assis'),
('J. R. R. Tolkien'),
('George R. R. Martin'),
('Agatha Christie');

-- LIVROS
INSERT INTO livro (titulo, editora, edicao, ano_publicacao) VALUES
('Dom Casmurro', 'Editora Globo', 1, '1899'),
('O Senhor dos Anéis', 'HarperCollins', 2, '1954'),
('O Hobbit', 'HarperCollins', 3, '1937'),
('Assassinato no Expresso Oriente', 'HarperCollins', 1, '1934'),
('A Guerra dos Tronos', 'Leya', 1, '1996');

-- ASSUNTOS
INSERT INTO assunto (descricao) VALUES
('Romance'),
('Fantasia'),
('Mistério'),
('Clássico'),
('Aventura');


-- RELACIONAMENTO LIVRO_AUTOR

-- Dom Casmurro - Machado de Assis
INSERT INTO livro_autor (id_livro, id_autor) VALUES (1, 1);

-- O Senhor dos Anéis - Tolkien
INSERT INTO livro_autor (id_livro, id_autor) VALUES (2, 2);

-- O Hobbit - Tolkien
INSERT INTO livro_autor (id_livro, id_autor) VALUES (3, 2);

-- Assassinato no Expresso Oriente - Agatha Christie
INSERT INTO livro_autor (id_livro, id_autor) VALUES (4, 4);

-- A Guerra dos Tronos - George R. R. Martin
INSERT INTO livro_autor (id_livro, id_autor) VALUES (5, 3);


-- RELACIONAMENTO LIVRO_ASSUNTO

-- Dom Casmurro
INSERT INTO livro_assunto (id_livro, id_assunto) VALUES (1, 1); -- Romance
INSERT INTO livro_assunto (id_livro, id_assunto) VALUES (1, 4); -- Clássico

-- Lord of the Rings
INSERT INTO livro_assunto (id_livro, id_assunto) VALUES (2, 2); -- Fantasia
INSERT INTO livro_assunto (id_livro, id_assunto) VALUES (2, 5); -- Aventura

-- The Hobbit
INSERT INTO livro_assunto (id_livro, id_assunto) VALUES (3, 2); -- Fantasia
INSERT INTO livro_assunto (id_livro, id_assunto) VALUES (3, 5); -- Aventura

-- Murder on the Orient Express
INSERT INTO livro_assunto (id_livro, id_assunto) VALUES (4, 3); -- Mistério

-- Game of Thrones
INSERT INTO livro_assunto (id_livro, id_assunto) VALUES (5, 2); -- Fantasia
INSERT INTO livro_assunto (id_livro, id_assunto) VALUES (5, 5); -- Aventura
