import { Routes } from '@angular/router';
import {LoginComponent} from './features/auth/login/login.component';
import {TaskListComponent} from './features/tasks/task-list/task-list.component';
import {AuthGuard} from './features/auth/auth.guard';
import {TaskEditComponent} from './features/tasks/task-edit/task-edit.component';
import {CategoryModalComponent} from './features/categories/category-modal/category-modal.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'tasks', component: TaskListComponent, canActivate: [AuthGuard] },
  { path: 'tasks/new', component: TaskEditComponent, canActivate: [AuthGuard] },
  { path: 'tasks/edit/:id', component: TaskEditComponent, canActivate: [AuthGuard] },
  { path: 'categories', component: CategoryModalComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: 'tasks', pathMatch: 'full' },
  { path: '**', redirectTo: 'tasks' }
];

