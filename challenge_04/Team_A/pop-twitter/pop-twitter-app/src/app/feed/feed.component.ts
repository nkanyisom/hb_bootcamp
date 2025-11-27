import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { CommonModule } from '@angular/common';
import { interval, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [MatCardModule, MatListModule, CommonModule],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.css'
})
export class FeedComponent implements OnInit, OnDestroy {
  posts: any[] = [];
  private subscription: Subscription | undefined;

  constructor(private apiService: ApiService) { }

  ngOnInit(): void {
    this.subscription = interval(2000)
      .pipe(
        switchMap(() => this.apiService.getFeed())
      )
      .subscribe(posts => {
        console.log(posts);
        this.posts = posts;
      });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
