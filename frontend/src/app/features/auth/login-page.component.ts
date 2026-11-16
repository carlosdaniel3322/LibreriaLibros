import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  imports: [ReactiveFormsModule],
  template: `
    <section class="login-shell"><form class="login-card" [formGroup]="form" (ngSubmit)="submit()">
      <p class="eyebrow">Acceso al sistema</p><h1>Biblioteca</h1><p class="muted">Inicia sesión para administrar el catálogo.</p>
      <label>Usuario<input formControlName="username" autocomplete="username" /></label>
      @if (show('username')) { <small class="field-error">Ingresa tu usuario.</small> }
      <label>Contraseña<input type="password" formControlName="password" autocomplete="current-password" /></label>
      @if (show('password')) { <small class="field-error">Ingresa tu contraseña.</small> }

      <button class="primary" [disabled]="loading">{{ loading ? 'Ingresando...' : 'Ingresar' }}</button>
      <p class="help">Usuarios: lector, editor o admin.</p>
    </form></section>
  `
})
export class LoginPageComponent {
  readonly form = new FormBuilder().nonNullable.group({ username: ['', Validators.required], password: ['', Validators.required] });
  loading = false;
  constructor(private readonly auth: AuthService, private readonly router: Router) {}
  show(control: 'username' | 'password') { const item = this.form.controls[control]; return item.invalid && (item.touched || item.dirty); }
  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading = true;
    const { username, password } = this.form.getRawValue();
    this.auth.login(username, password).subscribe({ next: () => void this.router.navigateByUrl('/'), error: () => { this.loading = false; } });
  }
}
