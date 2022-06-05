import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MiniUrlStoreService } from '../mini-url-store/mini-url-store.service';
import { Router } from '@angular/router';

const validUrlPattern = '(\b(https?|ftp|file)://)?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]';

@Component({
    selector: 'app-create-new-url',
    templateUrl: './create-new-url.component.html',
    styleUrls: ['./create-new-url.component.css'],
    providers: [MiniUrlStoreService]
})
export class CreateNewUrlComponent {

    generateUrlForm = this.formBuilder.group({
       longUrl: ['', [Validators.required, Validators.pattern(validUrlPattern)]]
    });

    constructor(
        private formBuilder: FormBuilder,
        private miniUrlStore: MiniUrlStoreService,
        private router: Router
    ) {}

    onSubmit(): void {
        this.miniUrlStore.createMiniUrl(this.generateUrlForm.value.longUrl!).subscribe({
            next: (stored) => this.router.navigate(['/shortened', stored.miniUrlCode]),
            error: (error) => console.warn(error)
        })
    }
}
