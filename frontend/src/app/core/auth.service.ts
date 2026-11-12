import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginResponse } from './models';

const API_URL = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly tokenState = signal(localStorage.getItem('biblioteca_token'));
  readonly token = computed(() => this.tokenState());
  readonly role = computed(() => this.readRole(this.tokenState()));
  readonly isLoggedIn = computed(() => Boolean(this.tokenState()));

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${API_URL}/auth/login`, { username, password }).pipe(
      tap(({ accessToken }) => {
        localStorage.setItem('biblioteca_token', accessToken);
        this.tokenState.set(accessToken);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('biblioteca_token');
    this.tokenState.set(null);
    void this.router.navigateByUrl('/login');
  }

  canEdit(): boolean { return ['EDITOR', 'ADMIN'].includes(this.role() ?? ''); }
  canDelete(): boolean { return this.role() === 'ADMIN'; }

  private readRole(token: string | null): string | null {
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
      return payload.roles?.[0] ?? null;
    } catch { return null; }
  }
}
