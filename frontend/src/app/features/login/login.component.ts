import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent {

  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
    this.http.post("http://localhost:8080/user/auth", { email, password }).subscribe((res: any) => {
      console.log(res);
    });
  }
}
