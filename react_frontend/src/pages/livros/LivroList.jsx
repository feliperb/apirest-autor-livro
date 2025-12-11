import React, { useEffect, useState } from 'react';
import { getLivros, deleteLivro } from '../../api/livroApi';
import { Link } from 'react-router-dom';

export default function LivroList(){
  const [items, setItems] = useState([]);
  useEffect(()=>{ load(); }, []);
  const load = async ()=>{ const r = await getLivros(); setItems(r.data); };
  const del = async (id)=>{ if(!confirm('Excluir?')) return; await deleteLivro(id); load(); };
  return (
    <div>
      <h1>Livros</h1>
      <Link to="/livros/novo">Novo Livro</Link>
      <ul>
        {items.map(l => <li key={l.id}>{l.titulo} - {l.editora} <Link to={`/livros/editar/${l.id}`}>Editar</Link> <button onClick={()=>del(l.id)}>Excluir</button></li>)}
      </ul>
    </div>
  );
}
