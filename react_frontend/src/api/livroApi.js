import api from './axios';
export const getLivros = () => api.get('/livros');
export const getLivroById = (id) => api.get(`/livros/${id}`);
export const createLivro = (data) => api.post('/livros', data);
export const updateLivro = (id, data) => api.put(`/livros/${id}`, data);
export const deleteLivro = (id) => api.delete(`/livros/${id}`);
