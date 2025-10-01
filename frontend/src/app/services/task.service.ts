import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';
import {Task} from '../models/task';
import {environment} from '../../environment';
import {Page} from '../models/page';

@Injectable({ providedIn: 'root' })
export class TaskService {
  private apiUrl = `${environment.apiUrl}/api/tasks`;

  constructor(private http: HttpClient) {}

  getFilteredTasks(
    filters: any,
    page: number = 0,
    size: number = 5,
    sortFields: string[] = []
  ): Observable<Page<Task>> {
    let params = new HttpParams()
      .set('page', (page ?? 0).toString())
      .set('size', (size ?? 5).toString())
      .set('completed', filters.completed !== undefined ? filters.completed : '')
      .set('startDate', filters.startDate ? filters.startDate.toISOString().split('T')[0] : '')
      .set('endDate', filters.endDate ? filters.endDate.toISOString().split('T')[0] : '')
      .set('category', filters.category || '');

    sortFields.forEach(sortField => {
      params = params.append('sort', sortField);
    });

    return this.http.get<Page<Task>>(this.apiUrl, { params });
  }

  get(id: string): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/${id}`);
  }

  create(task: Task): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, task);
  }

  update(task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${task.id}`, task);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
