import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class AppConfigurationService {
    apiURL: string = SERVER_API_URL;

    constructor(private http: HttpClient) {}

    getCarSegments() {
        return this.http.get(this.apiURL + '/api/administration/segment-depreciations');
    }

    updateCarSegments(payload) {
        return this.http.post(this.apiURL + '/api/administration/segment-depreciations', payload);
    }
}
