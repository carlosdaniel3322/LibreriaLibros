import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

const API_URL = 'http://localhost:8080/api';
const AUTH_URL = `${API_URL}/auth/`;

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const token = inject(AuthService).token();
  const isProtectedRequest = request.url.startsWith(API_URL) && !request.url.startsWith(AUTH_URL);

  return next(token && isProtectedRequest ? request.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : request);
};
