import { ExtraOption } from 'app/business/model/options/ExtraOption';

export class CarValuation {
    id: number;
    vehicleId: number;
    numberOfKilometers: number;
    fabricationDate: Date;
    registrationDate: Date;
    valuationDate: Date;

    options: Set<ExtraOption>;
}
