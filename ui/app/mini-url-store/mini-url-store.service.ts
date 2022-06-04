import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { throwError } from "rxjs";

@Injectable()
export class MiniUrlStoreService {
    serviceUrl = environment.apiEndpoint;

    constructor(private http: HttpClient) {}

    getMiniUrl(miniUrlCode : string) {
        return this.http.get<MiniUrl>(this.serviceUrl + '/' + miniUrlCode);
    }

    createMiniUrl(longUrl : string) {
        const newMinUrl : MiniUrl = {longUrl: longUrl, miniUrlCode: '', openCount: 0};
        return this.http.post<MiniUrl>(this.serviceUrl, newMinUrl)
            .pipe(
                catchError(this.handleError)
            );
    }

    updateMiniUrl(miniUrl: MiniUrl) {
        return this.http.put<MiniUrl>(this.serviceUrl + '/' + miniUrl.miniUrlCode, miniUrl)
            .pipe(
                catchError(this.handleError)
            );
    }

    private handleError(error: HttpErrorResponse) {
        if (error.status === 0) {
            // A client-side or network error occurred. Handle it accordingly.
            console.error('An error occurred:', error.error);
        } else {
            // The backend returned an unsuccessful response code.
            // The response body may contain clues as to what went wrong.
            console.error(
                `Backend returned code ${error.status}, body was: `, error.error);
        }
        // Return an observable with a user-facing error message.
        return throwError(() => new Error('Something bad happened; please try again later.'));
    }
}

export interface MiniUrl {
    longUrl: string;
    miniUrlCode: string;
    openCount: number;
}