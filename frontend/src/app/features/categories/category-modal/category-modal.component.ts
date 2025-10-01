
import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {FormGroup, FormBuilder, Validators, ReactiveFormsModule} from '@angular/forms';
import {TaskCategory} from '../../../models/task-category';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatListModule} from '@angular/material/list';
import {MatIconModule} from '@angular/material/icon';
import {TaskCategoryService} from '../../../services/task-category.service';

export interface CategoryModalData {
  categories: TaskCategory[];
}

@Component({
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatListModule,
    MatIconModule,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
  ],
  selector: 'app-category-modal',
  standalone: true,
  templateUrl: './category-modal.component.html',
})
export class CategoryModalComponent {
  categories: TaskCategory[];
  form: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<CategoryModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: CategoryModalData,
    private fb: FormBuilder,
    private taskCategoryService: TaskCategoryService
  ) {
    this.categories = [...data.categories];
    this.form = this.fb.group({
      name: ['', Validators.required]
    });
  }

  addCategory() {
    if (this.form.invalid) return;
    const category: TaskCategory = { name: this.form.value.name };
    this.taskCategoryService.create(category).subscribe(newCategory => {
      this.categories.push(newCategory);
      this.form.reset();
    });
  }

  deleteCategory(category: TaskCategory) {
    if (!category.id) return;
    this.taskCategoryService.delete(category.id).subscribe(() => {
      this.categories = this.categories.filter(c => c.id !== category.id);
    });
  }

  close() {
    this.dialogRef.close(true);
  }
}
