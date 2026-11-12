import { Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookFormComponent } from './book-form.component';
import { AuthService } from '../../core/auth.service';
import { CatalogService } from '../../core/catalog.service';
import { Libro, LibroPayload } from '../../core/models';

@Component({
  imports: [FormsModule, DecimalPipe, BookFormComponent],
  template: `
    <section class="page"><div class="page-heading"><div><p class="eyebrow">Catálogo</p><h1>Libros</h1><p class="muted">{{ books.length }} registro(s) disponibles.</p></div>@if (auth.canEdit()) { <button class="primary" (click)="openCreate()">Nuevo libro</button> }</div>
    <div class="toolbar"><input [(ngModel)]="query" (keyup.enter)="search()" placeholder="Buscar por título o autor" /><button class="secondary" (click)="search()">Buscar</button><button class="text-action" (click)="load()">Restablecer</button></div>
    @if (message) { <p class="alert" [class.error]="isError">{{ message }}</p> }
    @if (formOpen) { <app-book-form [book]="selected" (save)="save($event)" (cancel)="closeForm()" /> }
    <div class="data-table"><div class="table-row table-head"><span>Título</span><span>Autor</span><span>ISBN</span><span>Precio</span><span>Stock</span><span>Acciones</span></div>
      @for (book of books; track book.id) { <div class="table-row"><span><strong>{{ book.titulo }}</strong></span><span>{{ book.autor }}</span><span>{{ book.isbn }}</span><span>S/ {{ book.precio | number:'1.2-2' }}</span><span>{{ book.stock }}</span><span class="row-actions">@if (auth.canEdit()) { <button class="icon-button" title="Editar" (click)="edit(book)">Editar</button> } @if (auth.canDelete()) { <button class="icon-button danger" title="Eliminar" (click)="remove(book)">Eliminar</button> }</span></div> }
      @if (!books.length) { <div class="empty">No se encontraron libros.</div> }
    </div>
    </section>
  `
})
export class BooksPageComponent implements OnInit {
  books: Libro[] = []; selected: Libro | null = null; formOpen = false; query = ''; message = ''; isError = false;
  constructor(public readonly auth: AuthService, private readonly catalog: CatalogService) {}
  ngOnInit() { this.load(); }
  load() { this.catalog.listBooks().subscribe({ next: (data) => { this.books = data; this.query = ''; }, error: () => this.notify('No se pudo cargar el catálogo.', true) }); }
  search() { const text = this.query.trim(); if (!text) { this.load(); return; } this.catalog.searchBooks(text).subscribe({ next: (data) => this.books = data, error: () => this.notify('No se pudo realizar la búsqueda.', true) }); }
  openCreate() { this.selected = null; this.formOpen = true; this.message = ''; }
  edit(book: Libro) { this.selected = book; this.formOpen = true; this.message = ''; }
  closeForm() { this.formOpen = false; this.selected = null; }
  save(payload: LibroPayload) { this.catalog.saveBook(payload, this.selected?.id).subscribe({ next: () => { this.notify(this.selected ? 'Libro actualizado.' : 'Libro creado.'); this.closeForm(); this.load(); }, error: (error) => this.notify(error.status === 409 ? 'El ISBN ya está registrado.' : 'No se pudo guardar el libro.', true) }); }
  remove(book: Libro) { if (!confirm(`¿Eliminar "${book.titulo}"?`)) return; this.catalog.deleteBook(book.id).subscribe({ next: () => { this.notify('Libro eliminado.'); this.load(); }, error: () => this.notify('No se pudo eliminar el libro.', true) }); }
  private notify(message: string, isError = false) { this.message = message; this.isError = isError; }
}
