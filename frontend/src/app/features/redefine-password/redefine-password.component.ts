import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SnackbarService } from 'src/app/services/snackbar.service';

@Component({
  selector: 'app-redefine-password',
  templateUrl: './redefine-password.component.html',
  styleUrls: ['./redefine-password.component.less']
})
export class RedefinePasswordComponent {

  constructor(
    private route: ActivatedRoute,
    private snackbarService: SnackbarService,
    private http: HttpClient,
    private router: Router
  ) {}

  redefinePassword(newPassword: string, confirmNewPassword: string) {
    const roomId = this.route.snapshot.paramMap.get('id');
    if (!roomId) return;

    if (newPassword.trim().length == 0 || confirmNewPassword.trim().length == 0) {
      this.snackbarService.showMessage("Por favor, preencha todos os campos.", true);
      return;
    }

    if (newPassword != confirmNewPassword) {
      this.snackbarService.showMessage("As senhas não batem.", true);
      return;
    }

    this.http.post("auth/change-password", { roomId, newPassword }).subscribe(() => {
      alert("Se a sala for válida, a senha foi alterada com sucesso.")
      this.router.navigate(['/']);
    });
  }
}
