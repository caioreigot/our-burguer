import { Component } from '@angular/core';
import { SnackbarService } from 'src/app/services/snackbar.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.less']
})
export class RegisterComponent {

  emailPattern = "^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$";
  cpfPattern = "^[0-9]{3}\.?[0-9]{3}\.?[0-9]{3}\-?[0-9]{2}$";
  phonePattern = "^[(]?[0-9]{2}[)]?[ ]?[0-9]{4,5}[-]?[0-9]{4}$";

  constructor(private snackbarService: SnackbarService) {}

  onSubmit(
    name: string,
    email: string,
    password: string,
    confirmPassword: string,
    cpf: string,
    phone: string
  ) {
    const regexEmail = new RegExp(this.emailPattern);
    const regexCpf = new RegExp(this.cpfPattern);
    const regexPhone = new RegExp(this.phonePattern);
    
    if (!regexEmail.test(email)) {
      this.snackbarService.showMessage("O e-mail fornecido não é valido");
      return;
    }

    if (!regexCpf.test(cpf)) {
      this.snackbarService.showMessage("O CPF fornecido não é valido");
      return;
    }

    if (!regexPhone.test(phone)) {
      this.snackbarService.showMessage("O telefone fornecido não é valido");
      return;
    }

    if (confirmPassword !== password) {
      this.snackbarService.showMessage("As senhas não conferem", true);
      return false;
    }

    return false;
  }
}
