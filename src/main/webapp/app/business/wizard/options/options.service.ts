import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { StandardOption } from 'app/business/model/options/StandardOption';
import { ExtraOption } from 'app/business/model/options/ExtraOption';
import { CreateValuationDTO } from 'app/business/model/dto/CreateValuationDTO';
import { catchError, timeout } from 'rxjs/operators';

@Injectable()
export class OptionsService {
    apiURL: string = SERVER_API_URL;

    constructor(private httpClient: HttpClient) {}

    getStandardOptions(vehicleId: number): Observable<Array<StandardOption>> {
        return this.httpClient.get<Array<StandardOption>>(this.apiURL + '/api/options/standard/' + vehicleId);
    }

    getExtraOptions(vehicleId: number): Observable<Array<ExtraOption>> {
        return this.httpClient.get<Array<ExtraOption>>(this.apiURL + '/api/options/extra/' + vehicleId);
    }

    createValuation(payload: CreateValuationDTO) {
        return this.httpClient.post(this.apiURL + '/api/valuation', payload).pipe(
            timeout(1200000),
            catchError(e => {
                // do something on a timeout
                return of(null);
            })
        );
    }

    checkInclusion(optionId: number, vehicleId: number): Observable<any> {
        return this.httpClient.get(this.apiURL + '/api/options/includes/' + optionId + '/' + vehicleId);
    }

    checkExclusion(optionId: number, vehicleId: number): Observable<any> {
        return this.httpClient.get(this.apiURL + '/api/options/excludes/' + optionId + '/' + vehicleId);
    }
}
