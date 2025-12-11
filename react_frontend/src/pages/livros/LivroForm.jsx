import React, { useEffect, useState } from 'react';
import { createLivro, updateLivro, getLivroById } from '../../api/livroApi';
import { getAutores } from '../../api/autorApi';
import { getAssuntos } from '../../api/assuntoApi';
import { useNavigate, useParams } from 'react-router-dom';

export default function LivroForm(){
  const { id } = useParams();
  const isEdit = Boolean(id);
  const nav = useNavigate();
  const [form, setForm] = useState({ titulo:'', editora:'', edicao:1, anoPublicacao:'2023', idsAutores:[], idsAssuntos:[]});
  const [autores, setAutores] = useState([]);
  const [assuntos, setAssuntos] = useState([]);

  useEffect(()=>{
    getAutores().then(r=>setAutores(r.data)).catch(()=>{});
    getAssuntos().then(r=>setAssuntos(r.data)).catch(()=>{});
    if(isEdit) getLivroById(id).then(r=>setForm(r.data)).catch(()=>{});
  },[id]);

  const submit = async (e) => {
    e.preventDefault();
    try{
      if(isEdit) await updateLivro(id, form);
      else await createLivro(form);
      nav('/livros');
    }catch(err){ alert(err.response?.data?.message || 'Erro'); }
  };

  const toggle = (list, val) => list.includes(val) ? list.filter(x=>x!==val) : [...list, val];

  return (
    <div>
      <h1>{isEdit ? 'Editar Livro' : 'Novo Livro'}</h1>
      <form onSubmit={submit}>
        <div className="form-row">
          <input placeholder="Título" value={form.titulo} onChange={e=>setForm({...form,titulo:e.target.value})} />
        </div>
        <div className="form-row">
          <input placeholder="Editora" value={form.editora} onChange={e=>setForm({...form,editora:e.target.value})} />
        </div>
        <div className="form-row">
          <input placeholder="Edição" type="number" value={form.edicao} onChange={e=>setForm({...form,edicao:parseInt(e.target.value||0)})} />
        </div>
        <div className="form-row">
          <input placeholder="Ano" value={form.anoPublicacao} onChange={e=>setForm({...form,anoPublicacao:e.target.value})} />
        </div>

        <div>
          <h4>Autores</h4>
          {autores.map(a => <label key={a.id}><input type="checkbox" checked={form.idsAutores?.includes(a.id)} onChange={()=>setForm({...form, idsAutores: toggle(form.idsAutores||[], a.id)})} /> {a.nome}</label>)}
        </div>

        <div>
          <h4>Assuntos</h4>
          {assuntos.map(s => <label key={s.id}><input type="checkbox" checked={form.idsAssuntos?.includes(s.id)} onChange={()=>setForm({...form, idsAssuntos: toggle(form.idsAssuntos||[], s.id)})} /> {s.descricao}</label>)}
        </div>

        <button type="submit">Salvar</button>
      </form>
    </div>
  );
}
