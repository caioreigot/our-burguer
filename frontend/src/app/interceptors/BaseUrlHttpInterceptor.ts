import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders} from '@angular/common/http';
import { isDevMode, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from '../services/local-storage.service';

@Injectable()
export class BaseUrlHttpInterceptor implements HttpInterceptor {

  constructor(private localStorageService: LocalStorageService) {}

  backendBaseUrl = isDevMode() ? 'http://localhost:8080' : '';

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = this.localStorageService.getToken() ?? '';

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    })

    const apiRequest = request.clone({
      url: `${this.backendBaseUrl}/${request.url}`,
      headers
    });

    return next.handle(apiRequest);
  }
}