import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ValuationService {
    apiURL: string = SERVER_API_URL;

    constructor(private httpClient: HttpClient) {}

    getValuation(valuationId: number) {
        return this.httpClient.get(this.apiURL + '/api/valuation/' + valuationId);
    }

    getSimilarAds(valuationId: number) {
        return this.httpClient.get(this.apiURL + '/api/valuation/' + valuationId + '/ads');
    }

    getValuationInfo(valuationId: number) {
        return this.httpClient.get(this.apiURL + '/api/valuation/' + valuationId + '/info');
    }

    getExtraOptions(valuationId: number) {
        return this.httpClient.get(this.apiURL + '/api/valuation/' + valuationId + '/extra-options');
    }

    getMasterTypePoints(valuationId: number) {
        return this.httpClient.get(this.apiURL + '/api/valuation/' + valuationId + '/master-type');
    }

    getForecastTypePoints(valuationId: number, price: number) {
        return this.httpClient.get(this.apiURL + '/api/valuation/' + valuationId + '/forecast/' + price);
    }

    getAutovitParameters(valuationId: number) {
        return this.httpClient.get(this.apiURL + '/api/valuation/' + valuationId + '/autovit');
    }
}
