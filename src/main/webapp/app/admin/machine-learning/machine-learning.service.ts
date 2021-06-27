import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class MachineLearningService {
    apiURL: string = SERVER_API_URL;

    constructor(private http: HttpClient) {}

    getManufacturers() {
        return this.http.get(this.apiURL + '/api/data/manufacturers');
    }

    getModels(manufacturer: string) {
        const param: any = { manufacturer };

        return this.http.get(this.apiURL + '/api/data/models', { params: param });
    }

    getTrainingResults(manufacturer: string, model: string) {
        const param: any = { manufacturer, model };

        return this.http.get(this.apiURL + '/api/machine-learning/training-results', { params: param });
    }

    getMasterTypeData(manufacturer: string, model: string) {
        const param: any = { manufacturer, model };

        return this.http.get(this.apiURL + '/api/administration/master-types', { params: param });
    }

    getLock() {
        return this.http.get(this.apiURL + '/api/machine-learning/lock');
    }

    triggerTraining() {
        return this.http.post(this.apiURL + '/api/machine-learning/training', {});
    }
}
