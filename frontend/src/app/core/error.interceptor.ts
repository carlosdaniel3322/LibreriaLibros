import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { NotificationService } from './notification.service';

const MESSAGES: Record<number, string> = {
  400: 'Datos incorrectos',
  401: 'No autorizado',
  403: 'Acceso denegado',
  404: 'Recurso no encontrado',
  409: 'Conflicto con los datos',
  500: 'Error interno del servidor'
};

const FALLBACK_MESSAGE = 'Error inesperado';
const LOGIN_URL = '/auth/login';

export const errorInterceptor: HttpInterceptorFn = (request, next) => {
  const notifications = inject(NotificationService);
  const auth = inject(AuthService);

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      console.error('[HTTP]', error.status, request.method, request.urlWithParams, error);

      if (error.status === 0) {
        notifications.error('Error 0: Sin conexión con el servidor');
        return throwError(() => error);
      }

      const isLoginRequest = request.url.includes(LOGIN_URL);
      notifications.error(`Error ${error.status}: ${MESSAGES[error.status] ?? FALLBACK_MESSAGE}`);

      if (error.status === 401 && !isLoginRequest) {
        auth.logout();
      }

      return throwError(() => error);
    })
  );
};
