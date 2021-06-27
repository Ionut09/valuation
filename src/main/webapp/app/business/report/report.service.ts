import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ReportService {
    apiURL: string = SERVER_API_URL;

    constructor(private httpClient: HttpClient) {}

    getData(valuationId: number) {
        return this.httpClient.get(this.apiURL + '/api/report/' + valuationId);
    }

    getStdOptions(vehicleId: number) {
        return this.httpClient.get(this.apiURL + '/api/options/standard/' + vehicleId);
    }

    getExtraOptions(valuationId: number) {
        return this.httpClient.get(this.apiURL + '/api/valuation/' + valuationId + '/extra-options');
    }
}
