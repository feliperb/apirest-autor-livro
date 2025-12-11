import React, { useEffect, useState } from 'react';
import { createAutor, updateAutor, getAutorById } from '../../api/autorApi';
import { useNavigate, useParams } from 'react-router-dom';

export default function AutorForm(){
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();
  const [form, setForm] = useState({ nome: '' });

  useEffect(()=>{
    if(isEdit){
      getAutorById(id).then(r => setForm(r.data)).catch(()=>alert('Erro'));
    }
  },[id]);

  const submit = async (e) => {
    e.preventDefault();
    try{
      if(isEdit) await updateAutor(id, form);
      else await createAutor(form);
      navigate('/autores');
    }catch(err){
      alert(err.response?.data?.message || 'Erro ao salvar');
    }
  };

  return (
    <div>
      <h1>{isEdit ? 'Editar Autor' : 'Novo Autor'}</h1>
      <form onSubmit={submit}>
        <div className="form-row">
          <input placeholder="Nome" value={form.nome} onChange={e=>setForm({...form,nome:e.target.value})} />
        </div>
        <button type="submit">Salvar</button>
      </form>
    </div>
  );
}
