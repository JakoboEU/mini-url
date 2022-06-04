import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { CreateNewUrlComponent } from './mini-url-generation/create-new-url.component';
import { ShowUrlComponent } from './mini-url-generation/show-url.component';
import {GotoUrlComponent} from "./mini-url-generation/goto-url.component";
import {FourOhFourComponent} from "./error/404.component";

@NgModule({
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot([
      { path: '', component: CreateNewUrlComponent },
    ]),
    AppRoutingModule,
  ],
  declarations: [
    AppComponent,
    CreateNewUrlComponent,
    ShowUrlComponent,
    GotoUrlComponent,
    FourOhFourComponent,
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }


