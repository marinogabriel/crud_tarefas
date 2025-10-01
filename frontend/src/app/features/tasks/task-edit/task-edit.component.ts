import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {
  FormGroup,
  FormBuilder,
  Validators,
  ReactiveFormsModule,
  ValidationErrors,
  AbstractControl
} from '@angular/forms';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatInputModule} from '@angular/material/input';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonModule} from '@angular/material/button';
import {TaskCategory} from '../../../models/task-category';
import {Task} from '../../../models/task';
import {MatNativeDateModule} from '@angular/material/core';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';

export interface TaskEditData {
  task?: Task;
  categories: TaskCategory[];
}

@Component({
  selector: 'app-task-edit',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCheckboxModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
  ],
  templateUrl: './task-edit.component.html',
})
export class TaskEditComponent {
  form: FormGroup;
  categories: TaskCategory[];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<TaskEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: TaskEditData
  ) {
    this.categories = data.categories;
    this.form = this.fb.group({
      id: [data.task?.id],
      title: [data.task?.title ?? '', [Validators.required, Validators.maxLength(100)]],
      description: [data.task?.description ?? '', Validators.maxLength(500)],
      date: [data.task?.date ? new Date(data.task.date) : new Date(), [Validators.required, dateNotInPastValidator]],
      completed: [data.task?.completed ?? false],
      category: [data.task?.category ?? null, Validators.required]
    });
  }

  save() {
    if (this.form.valid) {
      const formValue = this.form.value;
      const task: Task = {
        id: formValue.id,
        title: formValue.title,
        description: formValue.description,
        date: formValue.date.toISOString().substring(0,10),
        completed: formValue.completed,
        category: formValue.category,
      };
      this.dialogRef.close(task);
    }
  }

  cancel() {
    this.dialogRef.close(undefined);
  }
}

function dateNotInPastValidator(control: AbstractControl): ValidationErrors | null {
  const selectedDate = control.value;
  if (!selectedDate) {
    return null;
  }

  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const selected = new Date(selectedDate);
  selected.setHours(0, 0, 0, 0);

  return selected >= today ? null : { dateInPast: true };
}
