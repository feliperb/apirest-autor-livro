import React, { useEffect, useState } from 'react';
import { getLivros, deleteLivro } from '../../api/livroApi';
import { Link } from 'react-router-dom';

export default function LivroList(){
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(()=>{ load(); }, []);

  const load = async ()=>{
    setLoading(true);
    setError(null);
    try{
      const r = await getLivros();
      let data = r?.data;
      if (Array.isArray(data)) setItems(data);
      else if (data && Array.isArray(data.content)) setItems(data.content);
      else if (data && Array.isArray(data.data)) setItems(data.data);
      else if (data == null) { setItems([]); setError('Resposta vazia do servidor'); }
      else { setItems([]); setError('Resposta inesperada do servidor: ' + JSON.stringify(data)); }
    }catch(e){
      console.error('Erro ao carregar livros', e);
      const msg = e?.response?.data?.message || e?.response?.data || e?.message || String(e);
      setError(msg);
    }finally{ setLoading(false); }
  };

  const del = async (id)=>{
    if(!confirm('Excluir?')) return;
    try{
      await deleteLivro(id);
      load();
    }catch(e){
      console.error('Erro ao excluir livro', e);
      const msg = e?.response?.data?.message || e?.response?.data || e?.message || String(e);
      alert('Erro ao excluir livro: ' + msg);
    }
  };

  return (
    <div>
      <h1>Livros</h1>
      <Link to="/livros/novo">Novo Livro</Link>
      {error && <p style={{color:'red'}}>Erro ao carregar livros: {typeof error === 'string' ? error : JSON.stringify(error)}</p>}
      {loading ? <p>Carregando...</p> : (
        <div>
          {items.length === 0 ? <p>Nenhum livro encontrado.</p> : (
            <ul>
              {items.map(l => (
                <li key={l.id} style={{marginBottom:16, borderBottom:'1px solid #ddd', paddingBottom:8}}>
                  <div style={{display:'flex',gap:8,alignItems:'center'}}>
                    <strong>{l.titulo || '—'}</strong>
                    <span>{l.editora ? `— ${l.editora}` : ''}</span>
                    <Link to={`/livros/editar/${l.id}`} style={{marginLeft:12}}>Editar</Link>
                    <button onClick={()=>del(l.id)} style={{marginLeft:8}}>Excluir</button>
                  </div>

                  <div style={{marginTop:8}}>
                    <div><strong>Edição:</strong> {l.edicao ?? '—'}</div>
                    <div><strong>Ano:</strong> {l.anoPublicacao ?? l.ano ?? '—'}</div>
                    <div><strong>Autores:</strong> {Array.isArray(l.autores) ? l.autores.map(a=>a.nome).join(', ') : (Array.isArray(l.idsAutores) ? l.idsAutores.join(', ') : (l.autores ? String(l.autores) : '—'))}</div>
                    <div><strong>Assuntos:</strong> {Array.isArray(l.assuntos) ? l.assuntos.map(s=>s.descricao).join(', ') : (Array.isArray(l.idsAssuntos) ? l.idsAssuntos.join(', ') : (l.assuntos ? String(l.assuntos) : '—'))}</div>
                  </div>

                  <details style={{marginTop:8}}>
                    <summary>Ver JSON completo</summary>
                    <pre style={{whiteSpace:'pre-wrap',background:'#f7f7f7',padding:8,borderRadius:4}}>{JSON.stringify(l, null, 2)}</pre>
                  </details>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
    </div>
  );
}
