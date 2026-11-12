import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './core/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    @if (auth.isLoggedIn()) {
      <header class="topbar">
        <a class="brand" routerLink="/">Biblioteca</a>
        <nav>
          <a routerLink="/libros" routerLinkActive="active">Libros</a>
          <a routerLink="/revistas" routerLinkActive="active">Revistas</a>
        </nav>
        <div class="account"><span>{{ auth.role() }}</span><button class="link-button" (click)="auth.logout()">Salir</button></div>
      </header>
    }
    <main [class.public-page]="!auth.isLoggedIn()"><router-outlet /></main>
  `
})
export class AppComponent { constructor(public readonly auth: AuthService) {} }
