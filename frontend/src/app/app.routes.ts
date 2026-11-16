import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';
import { LoginPageComponent } from './features/auth/login-page.component';
import { BooksPageComponent } from './features/books/books-page.component';
import { HomePageComponent } from './features/home/home-page.component';
import { MagazinesPageComponent } from './features/magazines/magazines-page.component';

export const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: '', component: HomePageComponent, canActivate: [authGuard] },
  { path: 'libros', component: BooksPageComponent, canActivate: [authGuard] },
  { path: 'revistas', component: MagazinesPageComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' }
];
