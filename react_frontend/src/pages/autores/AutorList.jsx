import React, { useEffect, useState } from 'react';
import { getAutores, deleteAutor } from '../../api/autorApi';
import { Link } from 'react-router-dom';

export default function AutorList(){
  const [autores, setAutores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(()=>{ load(); }, []);

  const load = async () => {
    setLoading(true);
    setError(null);
    try{
      const res = await getAutores();
      console.debug('getAutores response:', res);
      let data = res?.data;
      // normalize common response shapes to an array
      if (Array.isArray(data)) {
        setAutores(data);
      } else if (data && Array.isArray(data.content)) {
        setAutores(data.content);
      } else if (data && Array.isArray(data.data)) {
        setAutores(data.data);
      } else if (data == null) {
        setAutores([]);
        setError('Resposta vazia do servidor');
      } else {
        // unexpected shape - log and show informative error
        console.warn('Unexpected response shape for autores:', data);
        setAutores([]);
        setError('Resposta inesperada do servidor: ' + JSON.stringify(data));
      }
    }catch(e){
      console.error('Erro ao carregar autores', e);
      const msg = e?.response?.data?.message || e?.response?.data || e?.message || String(e);
      setError(msg);
    }finally{ setLoading(false); }
  };

  const handleDelete = async (id) => {
    if(!confirm('Excluir?')) return;
    await deleteAutor(id);
    load();
  };

  return (
    <div>
      <h1>Autores</h1>
      <Link to="/autores/novo">Novo Autor</Link>
      {error && <p style={{color:'red'}}>Erro ao carregar autores: {typeof error === 'string' ? error : JSON.stringify(error)}</p>}
      {loading ? <p>Carregando...</p> : (
        <table className="table">
          <thead><tr><th>Nome</th><th>Ações</th></tr></thead>
          <tbody>
            {autores.length === 0 ? (
              <tr><td colSpan={2}>Nenhum autor encontrado.</td></tr>
            ) : (
              autores.map(a => (
                <tr key={a.id}>
                  <td>{a.nome}</td>
                  <td>
                    <Link to={`/autores/editar/${a.id}`}>Editar</Link>
                    <button className="button" onClick={()=>handleDelete(a.id)}>Excluir</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}
