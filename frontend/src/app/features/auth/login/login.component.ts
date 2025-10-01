import { Component } from '@angular/core';

import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  imports: [ReactiveFormsModule, MatInputModule, MatButtonModule, MatCardModule, MatIconModule]
})
export class LoginComponent {
  form: FormGroup;
  isLoginMode: boolean = true;
  showPassword: boolean = false;
  showConfirmPassword: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      firstName: [''],
      lastName: [''],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['']
    });
  }

  onSubmit() {
    if (this.form.invalid) return;

    if (this.isLoginMode) {
      const loginData = {
        email: this.form.get('email')?.value,
        password: this.form.get('password')?.value,
      };
      this.authService.login(loginData).subscribe({
        next: (res) => {
          this.authService.saveToken(res.token);
          this.authService.saveRefreshToken(res.refreshToken);
          this.router.navigate(['/tasks']);
        },
        error: (err) => {
          console.error('Erro ao logar', err);
        }
      });
    } else {
      const registerData = {
        firstName: this.form.get('firstName')?.value,
        lastName: this.form.get('lastName')?.value,
        email: this.form.get('email')?.value,
        password: this.form.get('password')?.value,
        confirmPassword: this.form.get('confirmPassword')?.value,
      };
      this.authService.register(registerData).subscribe({
        next: (res) => {
          this.authService.saveToken(res.token);
          this.router.navigate(['/tasks']);
        },
        error: (err) => {
          console.error('Erro ao cadastrar', err);
        }
      });
    }

  }

  toggleMode() {
    this.isLoginMode = !this.isLoginMode;

    const nameControl = this.form.get('name');
    const confirmPasswordControl = this.form.get('confirmPassword');

    if (this.isLoginMode) {
      nameControl?.clearValidators();
      confirmPasswordControl?.clearValidators();
    } else {
      nameControl?.setValidators([
        Validators.required,
      ]);
      confirmPasswordControl?.setValidators([
        Validators.required,
        Validators.minLength(6)
      ]);
    }

    nameControl?.updateValueAndValidity();
    confirmPasswordControl?.updateValueAndValidity();
  }
}
