import {TaskCategory} from './task-category';
import {User} from './user';

export interface Task {
  id?: string;
  title: string;
  description?: string;
  date: string;
  completed: boolean;
  category: TaskCategory;
  user?: User;
}
