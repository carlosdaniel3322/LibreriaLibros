import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Libro, LibroPayload, Revista, RevistaPayload } from './models';

const API_URL = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class CatalogService {
  constructor(private readonly http: HttpClient) {}
  listBooks() { return this.http.get<Libro[]>(`${API_URL}/libros`); }
  saveBook(data: LibroPayload, id?: number) { return id ? this.http.put<Libro>(`${API_URL}/libros/${id}`, data) : this.http.post<Libro>(`${API_URL}/libros`, data); }
  deleteBook(id: number) { return this.http.delete<void>(`${API_URL}/libros/${id}`); }
  searchBooks(text: string) { return this.http.get<Libro[]>(`${API_URL}/libros/buscar`, { params: { texto: text } }); }
  listMagazines() { return this.http.get<Revista[]>(`${API_URL}/revistas`); }
  saveMagazine(data: RevistaPayload, id?: number) { return id ? this.http.put<Revista>(`${API_URL}/revistas/${id}`, data) : this.http.post<Revista>(`${API_URL}/revistas`, data); }
  deleteMagazine(id: number) { return this.http.delete<void>(`${API_URL}/revistas/${id}`); }
}
