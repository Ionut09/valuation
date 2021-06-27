import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { Manufacturer } from 'app/business/model/wizard/Manufacturer';
import { Model } from 'app/business/model/wizard/Model';
import { Body } from 'app/business/model/wizard/Body';
import { Generation } from 'app/business/model/wizard/Generation';
import { Vehicle } from 'app/business/model/wizard/Vehicle';

@Injectable()
export class WizardService {
    apiURL: string = SERVER_API_URL;

    constructor(private httpClient: HttpClient) {}

    getManufacturers(): Observable<Array<Manufacturer>> {
        return this.httpClient.get<Array<Manufacturer>>(this.apiURL + '/api/wizard/manufacturers');
    }

    getModelsOf(manufacturer: string): Observable<Array<Model>> {
        const param: any = { manufacturer };

        return this.httpClient.get<Array<Model>>(this.apiURL + '/api/wizard/models', { params: param });
    }

    getModelsIn(manufacturer: string, date: Date): Observable<Array<Model>> {
        const param: any = {
            manufacturer,
            date
        };

        return this.httpClient.get<Array<Model>>(this.apiURL + '/api/wizard/models', { params: param });
    }

    getFuelTypes(manufacturer: string, model: string, date?: Date): Observable<Array<string>> {
        let param: any;

        if (date) {
            param = {
                manufacturer,
                model,
                date
            };
        } else {
            param = {
                manufacturer,
                model
            };
        }

        return this.httpClient.get<Array<string>>(this.apiURL + '/api/wizard/fuels', { params: param });
    }

    getTransmissionTypes(manufacturer: string, model: string, fuel: string, date?: Date): Observable<Array<string>> {
        let param: any;

        if (date) {
            param = {
                manufacturer,
                model,
                fuel,
                date
            };
        } else {
            param = {
                manufacturer,
                model,
                fuel
            };
        }

        return this.httpClient.get<Array<string>>(this.apiURL + '/api/wizard/transmissions', { params: param });
    }

    getDrivenWheels(manufacturer: string, model: string, fuel: string, transmission: string, date?: Date): Observable<Array<string>> {
        let param: any;

        if (date) {
            param = {
                manufacturer,
                model,
                fuel,
                transmission,
                date
            };
        } else {
            param = {
                manufacturer,
                model,
                fuel,
                transmission
            };
        }

        return this.httpClient.get<Array<string>>(this.apiURL + '/api/wizard/driven-wheels', { params: param });
    }

    getBodyTypes(
        manufacturer: string,
        model: string,
        date?: string,
        fuelTypes?: string,
        transmissionTypes?: string,
        drivenWheels?: string
    ) {
        let param: any;

        if (date) {
            param = {
                manufacturer,
                model,
                date,
                fuelTypes,
                transmissionTypes,
                drivenWheels
            };
        } else {
            param = {
                manufacturer,
                model
            };
        }

        return this.httpClient.get<Array<Body>>(this.apiURL + '/api/wizard/bodies', { params: param });
    }

    getGenerations(
        manufacturer: string,
        model: string,
        body: string,
        doorsNo: number,
        date?: Date,
        fuelTypes?: string,
        transmissionTypes?: string,
        drivenWheels?: string
    ) {
        let param: any;

        if (date) {
            param = {
                manufacturer,
                model,
                body,
                doorsNo,
                date,
                fuelTypes,
                transmissionTypes,
                drivenWheels
            };
        } else {
            param = {
                manufacturer,
                model,
                body,
                doorsNo
            };
        }

        return this.httpClient.get<Array<Generation>>(this.apiURL + '/api/wizard/generations', { params: param });
    }

    getVehicles(
        manufacturer: string,
        model: string,
        body: string,
        doorsNo: number,
        generation: number,
        date?: string,
        fuelTypes?: string,
        transmissionTypes?: string,
        drivenWheels?: string
    ) {
        let param: any;

        if (fuelTypes) {
            param = {
                manufacturer,
                model,
                body,
                doorsNo,
                generation,
                date,
                fuelTypes,
                transmissionTypes,
                drivenWheels
            };
        } else {
            param = {
                manufacturer,
                model,
                body,
                doorsNo,
                generation
            };
        }

        return this.httpClient.get<Array<Vehicle>>(this.apiURL + '/api/wizard/vehicles', { params: param });
    }

    getVehiclesVinQuery(
        manufacturer: string,
        model: string,
        body: string,
        doorsNo: number,
        date?: string,
        registration?: string,
        fuelType?: string,
        horsePower?: number,
        engineKw?: number,
        gearbox?: string,
        cc?: number
    ) {
        let param;
        param = {
            manufacturer,
            model,
            body,
            doorsNo,
            date,
            registration,
            fuelType,
            horsePower,
            engineKw,
            gearbox,
            cc
        };

        Object.keys(param).forEach(key => param[key] === null && delete param[key]);

        return this.httpClient.get<Array<Vehicle>>(this.apiURL + '/api/wizard/vehicles-vin', { params: param });
    }

    queryVin(vin: string) {
        return this.httpClient.post(this.apiURL + '/api/vin/' + vin, {});
    }
}
