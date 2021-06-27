import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class HomeService {
    apiURL: string = SERVER_API_URL;

    constructor(private httpClient: HttpClient) {}

    getValuationHistory() {
        return this.httpClient.get(this.apiURL + '/api/valuation');
    }
}
