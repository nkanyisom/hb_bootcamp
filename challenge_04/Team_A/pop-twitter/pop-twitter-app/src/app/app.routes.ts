import { Routes } from '@angular/router';
import { SplashComponent } from './splash/splash.component';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';

export const routes: Routes = [
    { path: '', component: SplashComponent },
    { path: 'login', component: LoginComponent },
    { path: 'main', component: MainComponent }
];
