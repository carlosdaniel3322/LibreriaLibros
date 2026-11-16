import { Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { MagazineFormComponent } from './magazine-form.component';
import { AuthService } from '../../core/auth.service';
import { CatalogService } from '../../core/catalog.service';
import { Revista, RevistaPayload } from '../../core/models';

@Component({
  imports: [DecimalPipe, MagazineFormComponent],
  template: `
    <section class="page"><div class="page-heading"><div><p class="eyebrow">Catálogo</p><h1>Revistas</h1><p class="muted">{{ magazines.length }} registro(s) disponibles.</p></div>@if (auth.canEdit()) { <button class="primary" (click)="openCreate()">Nueva revista</button> }</div>
    @if (message) { <p class="alert">{{ message }}</p> }
    @if (formOpen) { <app-magazine-form [magazine]="selected" (save)="save($event)" (cancel)="closeForm()" /> }
    <div class="data-table"><div class="table-row table-head"><span>Título</span><span>Editorial</span><span>ISSN</span><span>Precio</span><span>Stock</span><span>Acciones</span></div>
      @for (magazine of magazines; track magazine.id) { <div class="table-row"><span><strong>{{ magazine.titulo }}</strong></span><span>{{ magazine.editorial }}</span><span>{{ magazine.issn }}</span><span>S/ {{ magazine.precio | number:'1.2-2' }}</span><span>{{ magazine.stock }}</span><span class="row-actions">@if (auth.canEdit()) { <button class="icon-button" (click)="edit(magazine)">Editar</button> } @if (auth.canDelete()) { <button class="icon-button danger" (click)="remove(magazine)">Eliminar</button> }</span></div> }
      @if (!magazines.length) { <div class="empty">No se encontraron revistas.</div> }
    </div></section>
  `
})
export class MagazinesPageComponent implements OnInit {
  magazines: Revista[] = []; selected: Revista | null = null; formOpen = false; message = '';
  constructor(public readonly auth: AuthService, private readonly catalog: CatalogService) {}
  ngOnInit() { this.load(); }
  load() { this.catalog.listMagazines().subscribe((data) => this.magazines = data); }
  openCreate() { this.selected = null; this.formOpen = true; this.message = ''; }
  edit(magazine: Revista) { this.selected = magazine; this.formOpen = true; this.message = ''; }
  closeForm() { this.formOpen = false; this.selected = null; }
  save(payload: RevistaPayload) { this.catalog.saveMagazine(payload, this.selected?.id).subscribe(() => { this.notify(this.selected ? 'Revista actualizada.' : 'Revista creada.'); this.closeForm(); this.load(); }); }
  remove(magazine: Revista) { if (!confirm(`¿Eliminar "${magazine.titulo}"?`)) return; this.catalog.deleteMagazine(magazine.id).subscribe(() => { this.notify('Revista eliminada.'); this.load(); }); }
  private notify(message: string) { this.message = message; }
}
