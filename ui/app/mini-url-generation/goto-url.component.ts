import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MiniUrl, MiniUrlStoreService} from '../mini-url-store/mini-url-store.service';

@Component({
    selector: 'app-goto-url',
    templateUrl: './goto-url.component.html',
    styleUrls: ['./goto-url.component.css'],
    providers: [MiniUrlStoreService]
})
export class GotoUrlComponent implements OnInit {

    constructor(
        private route: ActivatedRoute,
        private miniUrlStore: MiniUrlStoreService,
        private router: Router,
    ) {}

    ngOnInit(): void {
        const miniUrlCode = this.route.snapshot.paramMap.get('miniUrlCode')!
        this.miniUrlStore.getMiniUrl(miniUrlCode).subscribe({
            next: (stored) => this.updateOpenCountAndRedirect(stored),
            error: (error) => this.handleErrorWhenFetchingUrl(error)
        })
    }

    private updateOpenCountAndRedirect(stored: MiniUrl) {
        const miniUrl : MiniUrl = {miniUrlCode: stored.miniUrlCode, longUrl: stored.longUrl, openCount: stored.openCount + 1}
        this.miniUrlStore.updateMiniUrl(miniUrl).subscribe({
            next: (stored) => window.location.href = stored.longUrl,
            error: (error) => console.warn(error)
        });
    }

    private handleErrorWhenFetchingUrl(error: any) {
        if (error.status == 404) {
            return this.router.navigate(['/404'])
        } else {
            return console.warn(error)
        }
    }
}