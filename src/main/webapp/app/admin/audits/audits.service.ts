import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { createRequestOption } from 'app/shared';
import { SERVER_API_URL } from 'app/app.constants';
import { Audit } from './audit.model';

@Injectable({ providedIn: 'root' })
export class AuditsService {
    constructor(private http: HttpClient) {}

    query(req: any): Observable<HttpResponse<Audit[]>> {
        const params: HttpParams = createRequestOption(req);
        if (req.user) {
            params.set('user', req.user);
        }
        params.set('fromDate', req.fromDate);
        params.set('toDate', req.toDate);

        const requestURL = SERVER_API_URL + 'management/audits';

        return this.http.get<Audit[]>(requestURL, {
            params,
            observe: 'response'
        });
    }

    getUsers() {
        return this.http.get(SERVER_API_URL + 'management/audits/users');
    }

    downloadCSV(req: any) {
        const params: HttpParams = createRequestOption(req);

        params.set('user', req.user);
        params.set('fromDate', req.fromDate);
        params.set('toDate', req.toDate);

        return this.http.get(SERVER_API_URL + 'management/audits/export', {
            params,
            responseType: 'text'
        });
    }

    downloadAllCSV(req: any) {
        const params: HttpParams = createRequestOption(req);

        params.set('fromDate', req.fromDate);
        params.set('toDate', req.toDate);

        return this.http.get(SERVER_API_URL + 'management/audits/export/all', {
            params,
            responseType: 'text'
        });
    }
}
