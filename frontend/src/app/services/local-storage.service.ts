import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LocalStorageService {

  private NAME_ID = 'user_name';
  private TOKEN_ID = 'jwt_token';

  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_ID, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_ID);
  }

  clearToken(): void {
    localStorage.removeItem(this.TOKEN_ID);
  }

  setName(name: string): void {
    localStorage.setItem(this.NAME_ID, name);
  }

  getName(): string | null {
    return localStorage.getItem(this.NAME_ID);
  }
}