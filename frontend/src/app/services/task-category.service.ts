import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TaskCategory } from '../models/task-category';
import { Observable } from 'rxjs';
import {environment} from '../../environment';

@Injectable({ providedIn: 'root' })
export class TaskCategoryService {
  private apiUrl = `${environment.apiUrl}/api/task-categories`;

  constructor(private http: HttpClient) {}

  list(): Observable<TaskCategory[]> {
    return this.http.get<TaskCategory[]>(this.apiUrl);
  }

  create(category: TaskCategory): Observable<TaskCategory> {
    return this.http.post<TaskCategory>(this.apiUrl, category);
  }

  update(category: TaskCategory): Observable<TaskCategory> {
    return this.http.put<TaskCategory>(`${this.apiUrl}/${category.id}`, category);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
