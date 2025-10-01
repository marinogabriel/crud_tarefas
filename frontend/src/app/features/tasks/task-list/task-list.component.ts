import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule, PageEvent} from '@angular/material/paginator';
import {MatSortModule, Sort} from '@angular/material/sort';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {Task} from '../../../models/task';
import {TaskCategory} from '../../../models/task-category';
import {TaskService} from '../../../services/task.service';
import {TaskCategoryService} from '../../../services/task-category.service';
import {TaskEditComponent} from '../task-edit/task-edit.component';
import {CategoryModalComponent} from '../../categories/category-modal/category-modal.component';
import {CommonModule, NgClass} from '@angular/common';
import {Router} from '@angular/router';
import {MatFormFieldModule, MatLabel} from '@angular/material/form-field';
import {MatOption, MatSelect} from '@angular/material/select';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {FormsModule} from '@angular/forms';
import {MatInput} from '@angular/material/input';
import {MatNativeDateModule} from '@angular/material/core';
import {ConfirmationDialogComponent} from '../../confirmation-dialog/confirmation-dialog.component';

@Component({
  imports: [
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    MatDialogModule,
    CommonModule,
    NgClass,
    MatFormFieldModule,
    MatSelect,
    MatOption,
    MatDatepickerToggle,
    MatDatepicker,
    MatNativeDateModule,
    MatDatepickerInput,
    FormsModule,
    MatInput,
    MatLabel
  ],
  selector: 'app-task-list',
  standalone: true,
  templateUrl: './task-list.component.html',
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  categories: TaskCategory[] = [];

  pageSize = 5;
  pageIndex = 0;
  totalTasks = 0;

  sortField = '';
  sortDirection = '';

  displayedColumns: string[] = [
    'title',
    'date',
    'categoryName',
    'completed',
    'actions'
  ];
  dataSource: Task[] = [];

  filters = {
    completed: '',
    startDate: '',
    endDate: '',
    category: ''
  };

  constructor(
    private router: Router,
    private taskService: TaskService,
    private taskCategoryService: TaskCategoryService,
    private dialog: MatDialog,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadTasks();
  }

  loadTasks() {
    // Default sort
    const sortFields = this.sortField ? [this.sortField] : ['completed', 'date'];
    const sortDirections = this.sortDirection ? [this.sortDirection] : ['asc', 'asc'];

    // Build sort string params for backend
    const sortParams = sortFields.map((field, index) => `${field},${sortDirections[index] || 'asc'}`);

    this.taskService.getFilteredTasks(
      this.filters,
      this.pageIndex,
      this.pageSize,
      sortParams
    ).subscribe(response => {
      this.dataSource = response.content;
      this.pageIndex = response.number;
      this.pageSize = response.size;
      this.totalTasks = response.totalElements;
      this.cdRef.markForCheck();
    });
  }

  applyFilters() {
    this.pageIndex = 0;
    this.loadTasks();
  }

  clearFilters() {
    this.filters = {
      completed: '',
      startDate: '',
      endDate: '',
      category: ''
    };
    this.applyFilters();
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadTasks();
  }

  onSortChange(event: Sort) {
    this.pageIndex = 0;
    this.sortField = event.active;
    this.sortDirection = event.direction;
    this.loadTasks();
  }

  loadCategories() {
    this.taskCategoryService.list().subscribe(categories => (this.categories = categories));
  }

  openNewTask() {
    const dialogRef = this.dialog.open(TaskEditComponent, {
      width: '400px',
      data: { categories: this.categories }
    });
    dialogRef.afterClosed().subscribe((res: Task | undefined) => {
      if (res) {
        this.taskService.create(res).subscribe(() =>
          this.loadTasks());
      }
    });
  }

  openEditTask(task: Task) {
    const dialogRef = this.dialog.open(TaskEditComponent, {
      width: '400px',
      data: { task: task, categories: this.categories }
    });
    dialogRef.afterClosed().subscribe((res: Task | undefined) => {
      if (res) {
        this.taskService.update(res).subscribe(() => this.loadTasks());
      }
    });
  }

  openCategoryModal() {
    const dialogRef = this.dialog.open(CategoryModalComponent, {
      width: '400px',
      data: { categories: this.categories }
    });
    dialogRef.afterClosed().subscribe((changed: boolean) => {
      if (changed) {
        this.loadCategories();
      }
    });
  }

  toggleComplete(task: Task) {
    const updated = { ...task, completed: !task.completed };
    this.taskService.update(updated).subscribe(() => this.loadTasks());
  }

  isOverdue(task: Task): boolean {
    if (task.completed || !task.date) return false;

    const taskDate = new Date(task.date);
    const today = new Date();

    const taskDateString = taskDate.toISOString().split('T')[0];
    const todayString = today.toISOString().split('T')[0];

    return taskDateString < todayString;
  }

  openDeleteConfirmation(task: Task): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '300px',
      data: {
        title: "Confirmação",
        message: 'Confirma a exclusão dessa tarefa?'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.delete(task);
      }
    });
  }

  delete(task: Task) {
    if (task.id) {
      this.taskService.delete(task.id).subscribe(() => this.loadTasks());
    }
  }

  logout() {
    localStorage.removeItem('authToken');
    this.router.navigate(['/login']);
  }
}
