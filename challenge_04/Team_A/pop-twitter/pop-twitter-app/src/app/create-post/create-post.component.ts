import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ApiService } from '../api.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent implements OnInit{
  postContent = '';
  user: any

  constructor(private apiService: ApiService, private snackBar: MatSnackBar) {

   }

  ngOnInit(): void {
      this.user = this.apiService.getUser() || '{}'
      console.log(this.user);
      
  }

  createPost() {
    const user = this.apiService.getUser() || '{}';
    this.apiService.createPost(user.id, this.postContent).subscribe(() => {
      this.postContent = '';
      this.snackBar.open('Post created successfully!', 'Close', { duration: 3000 });
    });
  }
}
