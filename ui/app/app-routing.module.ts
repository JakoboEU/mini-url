import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {CreateNewUrlComponent} from "./mini-url-generation/create-new-url.component";
import {ShowUrlComponent} from "./mini-url-generation/show-url.component";
import {GotoUrlComponent} from "./mini-url-generation/goto-url.component";
import {FourOhFourComponent} from "./error/404.component";

export const routes: Routes = [
    {path: '' , component: CreateNewUrlComponent},
    {path: 'shortened/:miniUrlCode' , component: ShowUrlComponent},
    {path: 'goto/:miniUrlCode', component: GotoUrlComponent},
    {path: '404', component: FourOhFourComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {

}