import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class DataIngestService {
    apiURL: string = SERVER_API_URL;

    static toFormData<T>(formValue: T) {
        const formData = new FormData();

        for (const key of Object.keys(formValue)) {
            const value = formValue[key];
            formData.append(key, value);
        }

        return formData;
    }

    constructor(private http: HttpClient) {}

    uploadCSV(data) {
        return this.http.post(this.apiURL + '/api/administration/ingest/autovit', DataIngestService.toFormData(data), {
            reportProgress: true,
            observe: 'events'
        });
    }

    uploadCSV2(data) {
        return this.http.post(this.apiURL + '/api/administration/ingest/tradein', DataIngestService.toFormData(data), {
            reportProgress: true,
            observe: 'events'
        });
    }

    uploadCSV3(data) {
        return this.http.post(this.apiURL + '/api/administration/ingest/autovit-excel', DataIngestService.toFormData(data), {
            reportProgress: true,
            observe: 'events'
        });
    }

    getLock() {
        return this.http.get(this.apiURL + '/api/administration/ingest/autovit/lock');
    }

    getLock2() {
        return this.http.get(this.apiURL + '/api/administration/ingest/tradein/lock');
    }

    getLock3() {
        return this.http.get(this.apiURL + '/api/administration/ingest/autovit-excel/lock');
    }
}
