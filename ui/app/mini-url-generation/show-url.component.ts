import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MiniUrl, MiniUrlStoreService} from '../mini-url-store/mini-url-store.service';

@Component({
    selector: 'app-show-url',
    templateUrl: './show-url.component.html',
    styleUrls: ['./show-url.component.css'],
    providers: [MiniUrlStoreService]
})
export class ShowUrlComponent implements OnInit {
    miniUrl: MiniUrl = {longUrl: '', miniUrlCode: '', openCount: 0}

    constructor(
        private route: ActivatedRoute,
        private miniUrlStore: MiniUrlStoreService,
        private router: Router
    ) {}

    ngOnInit(): void {
        const miniUrlCode = this.route.snapshot.paramMap.get('miniUrlCode')!
        this.miniUrlStore.getMiniUrl(miniUrlCode).subscribe({
            next: (stored) => this.miniUrl = stored,
            error: (error) => this.handleErrorWhenFetchingUrl(error)
        })
    }

    private handleErrorWhenFetchingUrl(error: any) {
        if (error.status == 404) {
            return this.router.navigate(['/404'])
        } else {
            return console.warn(error)
        }
    }
}