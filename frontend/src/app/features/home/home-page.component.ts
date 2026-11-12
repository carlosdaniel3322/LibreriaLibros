import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  imports: [RouterLink],
  template: `
    <section class="page home"><p class="eyebrow">Panel principal</p><h1>Bienvenido, {{ auth.role() }}</h1><p class="muted">Gestiona el catálogo persistente de la biblioteca.</p>
      <div class="quick-links"><a routerLink="/libros"><strong>Libros</strong><span>Consultar, buscar y administrar libros.</span></a><a routerLink="/revistas"><strong>Revistas</strong><span>Consultar y administrar revistas.</span></a></div>
    </section>
  `
})
export class HomePageComponent { constructor(public readonly auth: AuthService) {} }
