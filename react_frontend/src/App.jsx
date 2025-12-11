import React from "react";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import AutorList from './pages/autores/AutorList';
import AutorForm from './pages/autores/AutorForm';
import LivroList from './pages/livros/LivroList';
import LivroForm from './pages/livros/LivroForm';
import AssuntoList from './pages/assuntos/AssuntoList';
import AssuntoForm from './pages/assuntos/AssuntoForm';

export default function App(){
  return (
    <BrowserRouter>
      <nav className="nav">
        <Link to="/autores">Autores</Link>
        <Link to="/livros">Livros</Link>
        <Link to="/assuntos">Assuntos</Link>
      </nav>
      <main className="container">
        <Routes>
          <Route path="/" element={<AutorList/>} />
          <Route path="/autores" element={<AutorList/>} />
          <Route path="/autores/novo" element={<AutorForm/>} />
          <Route path="/autores/editar/:id" element={<AutorForm/>} />
          <Route path="/livros" element={<LivroList/>} />
          <Route path="/livros/novo" element={<LivroForm/>} />
          <Route path="/livros/editar/:id" element={<LivroForm/>} />
          <Route path="/assuntos" element={<AssuntoList/>} />
          <Route path="/assuntos/novo" element={<AssuntoForm/>} />
          <Route path="/assuntos/editar/:id" element={<AssuntoForm/>} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}
