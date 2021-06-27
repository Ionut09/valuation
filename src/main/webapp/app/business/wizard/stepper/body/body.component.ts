import { Component, EventEmitter, Input, Output } from '@angular/core';
import { WizardService } from 'app/business/wizard/wizard.service';
import { Body } from 'app/business/model/wizard/Body';

import * as moment from 'moment';

@Component({
    selector: 'jhi-car-body',
    templateUrl: './body.component.html',
    styleUrls: ['body.component.scss']
})
export class CarBodyComponent {
    @Input() state: any;
    @Output() bodySelected = new EventEmitter();

    bodies: Array<Body> = [];
    dataLoaded = false;

    constructor(private wizardService: WizardService) {}

    fetchData() {
        this.dataLoaded = false;
        this.bodies = [];

        let date;

        if (moment(this.state.manufacturingDate).year() === moment(this.state.registrationDate).year()) {
            date = this.state.registrationDate;
        } else {
            date = this.state.manufacturingDate;
        }

        this.wizardService
            .getBodyTypes(
                this.state.model.manufacturer,
                this.state.model.model,
                date,
                this.state.selectedFuelTypes,
                this.state.selectedTransmissionTypes,
                this.state.selectedDrivenWheels
            )
            .subscribe(response => {
                this.dataLoaded = true;
                this.bodies = response;
            });
    }

    handleBodySelection(body: Body) {
        this.state.body = body;
        this.bodySelected.emit();
    }

    getImageUrl(body: Body) {
        return 'photos/' + body.imageUrl + '.JPG';
    }
}
