import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { SnackbarService } from 'src/app/services/snackbar.service';

@Component({
  selector: 'app-forgot-my-password',
  templateUrl: './forgot-my-password.component.html',
  styleUrls: ['./forgot-my-password.component.less']
})
export class ForgotMyPasswordComponent {
  
  regexEmail = new RegExp(/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/);
  
  constructor(
    private http: HttpClient,
    private snackbarService: SnackbarService
  ) {}

  sendEmail(email: string) {
    // Ve se email.value é um email válido com regex
    if (this.regexEmail.test(email)) {
      this.http.post('auth/forgot-password', { email }).subscribe();
      this.snackbarService.showMessage('Um e-mail para redefinição de senha foi enviado');
    } else {
      this.snackbarService.showMessage('E-mail inválido', true);
    }
  }
}