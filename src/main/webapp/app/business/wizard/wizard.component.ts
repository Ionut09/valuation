import { Component, OnInit, ViewChild } from '@angular/core';
import { WizardOptionsComponent } from 'app/business/wizard/options/options.component';
import { ActivatedRoute } from '@angular/router';
import { WizardStepperComponent } from 'app/business/wizard/stepper/stepper.component';

@Component({
    selector: 'jhi-wizard',
    templateUrl: './wizard.component.html',
    styleUrls: ['wizard.component.scss']
})
export class WizardComponent implements OnInit {
    showVin: boolean;
    showStepper: boolean;
    showOptions: boolean;

    stepperState: any = {};

    @ViewChild(WizardOptionsComponent)
    wizardOptionsView: WizardOptionsComponent;

    @ViewChild(WizardStepperComponent)
    stepperView: WizardStepperComponent;

    constructor(private route: ActivatedRoute) {}

    ngOnInit(): void {
        if (
            this.route.snapshot.queryParams['make'] &&
            this.route.snapshot.queryParams['model'] &&
            this.route.snapshot.queryParams['optionals']
        ) {
            const make = this.route.snapshot.queryParamMap.get('make');
            const model = this.route.snapshot.queryParamMap.get('model');
            const optionals = this.route.snapshot.queryParamMap.get('optionals');

            console.log(make);
            console.log(model);
            console.log(optionals);
        } else {
            this.showVin = true;

            this.showStepper = false;
            this.showOptions = false;
        }
    }

    handleManualSearch() {
        this.showVin = false;
        this.showOptions = false;

        this.showStepper = true;
    }

    handleVinSearch(data) {
        console.log('Wizard: handle vin query');
        this.showVin = false;

        this.showStepper = true;

        // move to last step and load data
        setTimeout(() => this.stepperView.moveToLastStepFromVIN(data));
    }

    handleStepperFinish(data) {
        console.log(data);
        this.stepperState = data.state;

        this.showVin = false;
        this.showStepper = false;

        this.showOptions = true;

        setTimeout(() => this.wizardOptionsView.fetchData(data.extraOptionCodes));
    }
}
