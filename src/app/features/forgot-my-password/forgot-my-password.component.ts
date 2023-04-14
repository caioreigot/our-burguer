import { Component } from '@angular/core';
import { SnackbarService } from 'src/app/services/snackbar.service';

@Component({
  selector: 'app-forgot-my-password',
  templateUrl: './forgot-my-password.component.html',
  styleUrls: ['./forgot-my-password.component.less']
})
export class ForgotMyPasswordComponent {

  regexEmail = new RegExp(/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/);

  constructor(private snackbarService: SnackbarService) {}

  sendEmail(email: string) {
    // veja se email.value é um email válido com regex
    if (this.regexEmail.test(email)) {
      this.snackbarService.showMessage('Email enviado com sucesso!');
    } else {
      this.snackbarService.showMessage('Email inválido!', true);
    }
  }
}