import { Component } from '@angular/core';
import { CreatePostComponent } from '../create-post/create-post.component';
import { UserPostsComponent } from '../user-posts/user-posts.component';
import { FeedComponent } from '../feed/feed.component';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [CreatePostComponent, UserPostsComponent, FeedComponent],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent {

}
