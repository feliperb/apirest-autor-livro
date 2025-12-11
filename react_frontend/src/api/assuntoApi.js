import api from './axios';
export const getAssuntos = () => api.get('/assuntos');
export const getAssuntoById = (id) => api.get(`/assuntos/${id}`);
export const createAssunto = (data) => api.post('/assuntos', data);
export const updateAssunto = (id, data) => api.put(`/assuntos/${id}`, data);
export const deleteAssunto = (id) => api.delete(`/assuntos/${id}`);
