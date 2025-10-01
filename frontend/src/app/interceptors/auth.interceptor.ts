import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

import { inject } from '@angular/core';
import {
  HttpInterceptorFn,
  HttpHandlerFn, HttpErrorResponse, HttpRequest,
} from '@angular/common/http';
import { catchError, filter, switchMap, take } from 'rxjs/operators';

let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  const token = authService.getToken();

  let authReq = req;
  if (token) {
    authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        return handle401(authReq, next, authService);
      }
      return throwError(() => error);
    })
  );
};

function handle401(
  request: HttpRequest<any>,
  next: HttpHandlerFn,
  authService: AuthService
): Observable<any> {
  if (!isRefreshing) {
    isRefreshing = true;
    refreshTokenSubject.next(null);

    const refreshToken = authService.getRefreshToken();
    if (!refreshToken) {
      authService.logout();
      return throwError(() => new Error('No refresh token found'));
    }

    return authService.refreshToken(refreshToken).pipe(
      switchMap((res: any) => {
        isRefreshing = false;
        authService.saveToken(res.token);
        authService.saveRefreshToken(res.refreshToken);
        refreshTokenSubject.next(res.token);

        const retryReq = request.clone({
          headers: request.headers.set('Authorization', `Bearer ${res.token}`)
        });
        return next(retryReq);
      }),
      catchError(err => {
        isRefreshing = false;
        authService.logout();
        return throwError(() => err);
      })
    );
  } else {
    return refreshTokenSubject.pipe(
      filter(token => token != null),
      take(1),
      switchMap(token => {
        const retryReq = request.clone({
          headers: request.headers.set('Authorization', `Bearer ${token}`)
        });
        return next(retryReq);
      })
    );
  }
}

