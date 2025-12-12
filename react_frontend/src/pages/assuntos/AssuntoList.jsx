import React, { useEffect, useState } from 'react';
import { getAssuntos, deleteAssunto } from '../../api/assuntoApi';
import { Link } from 'react-router-dom';

export default function AssuntoList(){
  const [items, setItems] = useState([]);
  useEffect(()=>{ load(); }, []);
  const load = async ()=>{ const r = await getAssuntos(); setItems(r.data); };
  const del = async (id)=>{
    if(!confirm('Excluir?')) return;
    try{
      await deleteAssunto(id);
      load();
    }catch(e){
      console.error('Erro ao excluir assunto', e);
      const msg = e?.response?.data?.message || e?.response?.data || e?.message || String(e);
      alert('Erro ao excluir assunto: ' + msg);
    }
  };
  return (
    <div>
      <h1>Assuntos</h1>
      <Link to="/assuntos/novo">Novo Assunto</Link>
      <ul>
        {items.map(a => <li key={a.id}>{a.descricao} <Link to={`/assuntos/editar/${a.id}`}>Editar</Link> <button onClick={()=>del(a.id)}>Excluir</button></li>)}
      </ul>
    </div>
  );
}
