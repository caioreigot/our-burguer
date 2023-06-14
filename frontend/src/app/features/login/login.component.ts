import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { LocalStorageService } from 'src/app/services/local-storage.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { Router } from "@angular/router"

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {

  constructor(
    private http: HttpClient,
    private snackbarService: SnackbarService,
    private localStorageService: LocalStorageService,
    private router: Router
  ) {}

  // Se já houver um token salvo no local storage, tenta autenticar com ele
  ngOnInit(): void {
    const token = this.localStorageService.getToken();
    if (!token) return;
    
    this.http.post("auth/jwt", token).subscribe({
      next: (response: any) => {
        this.localStorageService.setName(response.name);
        this.router.navigate(['/shop-window']);
      },
      error: (response: any) => {
        this.snackbarService.showMessage(response.error, true);
      }
    });
  }

  login(email: string, password: string) {
    this.http.post("auth", { email, password })
      .subscribe({
        next: (response: any) => {
          this.localStorageService.setToken(response.token);
          this.localStorageService.setName(response.name);
          this.router.navigate(['/shop-window']);
        },
        error: (response: any) => {
          this.snackbarService.showMessage(response.error, true);
        }
      });
  }
}
