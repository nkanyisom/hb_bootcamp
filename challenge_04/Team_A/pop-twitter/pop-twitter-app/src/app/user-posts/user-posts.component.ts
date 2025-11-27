import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api.service';
import { interval, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-user-posts',
  standalone: true,
  imports: [MatCardModule, MatListModule, CommonModule],
  templateUrl: './user-posts.component.html',
  styleUrl: './user-posts.component.css'
})
export class UserPostsComponent implements OnInit, OnDestroy {
  posts: any[] = [];
  private subscription: Subscription | undefined;

  constructor(private apiService: ApiService) { }

  ngOnInit(): void {
    const user =this.apiService.getUser() || '{}';
    this.subscription = interval(2000)
      .pipe(
        switchMap(() => this.apiService.getPostsByUserId(user.id))
      )
      .subscribe(posts => {
        this.posts = posts;
      });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
