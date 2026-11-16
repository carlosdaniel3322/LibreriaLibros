export interface Libro { id: number; titulo: string; autor: string; isbn: string; precio: number; stock: number; }
export interface Revista { id: number; titulo: string; editorial: string; issn: string; precio: number; stock: number; }
export type LibroPayload = Omit<Libro, 'id'>;
export type RevistaPayload = Omit<Revista, 'id'>;
export interface LoginResponse { accessToken: string; }
