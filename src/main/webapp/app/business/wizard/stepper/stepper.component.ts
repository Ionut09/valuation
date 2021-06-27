import { Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { CarModelComponent } from 'app/business/wizard/stepper/model/model.component';
import { MatStepper } from '@angular/material';
import { Model } from 'app/business/model/wizard/Model';
import { FuelTractionComponent } from 'app/business/wizard/stepper/fuel/fuel-traction.component';
import { CarBodyComponent } from 'app/business/wizard/stepper/body/body.component';
import { Body } from 'app/business/model/wizard/Body';
import { CarGenerationComponent } from 'app/business/wizard/stepper/generation/generation.component';
import { Generation } from 'app/business/model/wizard/Generation';
import { CarVersionComponent } from 'app/business/wizard/stepper/version/version.component';
import { Vehicle } from 'app/business/model/wizard/Vehicle';

@Component({
    selector: 'jhi-wizard-stepper',
    templateUrl: './stepper.component.html',
    styleUrls: ['stepper.component.scss']
})
export class WizardStepperComponent implements OnInit {
    isLinear = false;

    state = {
        manufacturer: '',
        manufacturingYear: '',
        registrationYear: '',
        manufacturingDate: '',
        registrationDate: '',

        model: Model,
        body: Body,
        generation: Generation,
        car: Vehicle,

        selectedFuelTypes: [],
        selectedTransmissionTypes: [],
        selectedDrivenWheels: []
    };

    @ViewChild(CarModelComponent)
    carModelView: CarModelComponent;

    @ViewChild(FuelTractionComponent)
    carFuelView: FuelTractionComponent;

    @ViewChild(CarBodyComponent)
    carBodyView: CarBodyComponent;

    @ViewChild(CarGenerationComponent)
    carGenerationView: CarGenerationComponent;

    @ViewChild(CarVersionComponent)
    carVersionView: CarVersionComponent;

    @ViewChild(MatStepper)
    stepper: MatStepper;

    @Output() stepperFinished = new EventEmitter();

    itemsDisabled = false;
    vinData;

    constructor() {}

    ngOnInit() {}

    handleManufacturerSelection(event) {
        console.log(event);

        this.state.manufacturer = event.manufacturer;

        this.state.manufacturingYear = event.manufacturingYear;
        this.state.registrationYear = event.registrationYear;

        this.state.manufacturingDate = event.manufacturingDate;
        this.state.registrationDate = event.registrationDate;
    }

    triggerNextStep() {
        this.stepper.next();
    }

    stepChanged(event) {
        if (event.selectedIndex === 1) {
            console.log('Fetching car models...');
            this.carModelView.fetchData();
        }

        if (event.selectedIndex === 2) {
            console.log('Fetching car body types...');
            this.carBodyView.fetchData();
        }

        if (event.selectedIndex === 3) {
            console.log('Fetching generations...');
            this.carGenerationView.fetchData();
        }

        if (event.selectedIndex === 4) {
            if (!this.itemsDisabled) {
                console.log('Fetching vehicles...');
                setTimeout(() => this.carVersionView.fetchData());
            } else {
                console.log('[VinQuery] Fetching vehicles...');
                setTimeout(() => this.carVersionView.fetchDataFromVIN(this.vinData));
            }
        }
    }

    handleCarSelection(payload) {
        this.state.car = payload.car;

        const _payload = {
            state: this.state,
            extraOptionCodes: payload.extraOptionCodes
        };

        this.stepperFinished.emit(_payload);
    }

    moveToLastStepFromVIN(data) {
        console.log('Stepper: move to last step');
        this.itemsDisabled = true;
        this.vinData = data;
        this.stepper.selectedIndex = 4;

        console.log(data);
    }
}
