import { ExtraOption } from 'app/business/model/options/ExtraOption';

export class CreateValuationDTO {
    vehicleId: number;
    numberOfKilometers: number;
    attritionValue: number;
    bodyDamageValue: number;
    gpl: boolean;

    fabricationDate: Date;
    registrationDate: Date;

    options: Array<ExtraOption>;
}
