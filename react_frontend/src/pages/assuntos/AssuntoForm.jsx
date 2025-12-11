import React, { useEffect, useState } from 'react';
import { createAssunto, updateAssunto, getAssuntoById } from '../../api/assuntoApi';
import { useNavigate, useParams } from 'react-router-dom';

export default function AssuntoForm(){
  const { id } = useParams();
  const isEdit = Boolean(id);
  const [form, setForm] = useState({ descricao: ''});
  const nav = useNavigate();

  useEffect(()=>{ if(isEdit) getAssuntoById(id).then(r=>setForm(r.data)).catch(()=>alert('Erro')) },[id]);

  const submit = async (e) => {
    e.preventDefault();
    try{
      if(isEdit) await updateAssunto(id, form);
      else await createAssunto(form);
      nav('/assuntos');
    }catch(err){ alert(err.response?.data?.message || 'Erro'); }
  };

  return (
    <div>
      <h1>{isEdit ? 'Editar Assunto' : 'Novo Assunto'}</h1>
      <form onSubmit={submit}>
        <input placeholder="Descrição" value={form.descricao} onChange={e=>setForm({...form,descricao:e.target.value})} />
        <button type="submit">Salvar</button>
      </form>
    </div>
  );
}
