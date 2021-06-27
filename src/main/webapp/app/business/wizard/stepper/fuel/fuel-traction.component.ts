import { Component, Input } from '@angular/core';
import { WizardService } from 'app/business/wizard/wizard.service';
import * as moment from 'moment';

@Component({
    selector: 'jhi-car-fuel-traction',
    templateUrl: './fuel-traction.component.html',
    styleUrls: ['fuel-traction.component.scss']
})
export class FuelTractionComponent {
    allTransmissions = [];
    allDrivenWheels = [];
    allFuelTypes = [];

    dataLoaded = false;

    @Input() state: any;

    constructor(private wizardService: WizardService) {}

    fetchData(option?: string) {
        let date;

        if (moment(this.state.manufacturingDate).year() === moment(this.state.registrationDate).year()) {
            date = this.state.registrationDate;
        } else {
            date = this.state.manufacturingDate;
        }

        if (option === undefined || option === 'fuel') {
            this.wizardService.getFuelTypes(this.state.model.manufacturer, this.state.model.model, date).subscribe(response => {
                this.dataLoaded = true;
                this.allFuelTypes = response;
            });
        }

        if (option === 'transmission') {
            this.dataLoaded = false;
            this.state.selectedFuelTypes.forEach(ft => {
                this.wizardService
                    .getTransmissionTypes(this.state.model.manufacturer, this.state.model.model, ft, date)
                    .subscribe(response => {
                        this.dataLoaded = true;
                        this.allTransmissions = this.arrayUnique(this.allTransmissions.concat(response));
                    });
            });
        }

        if (option === 'drivenWheels') {
            this.dataLoaded = false;
            this.state.selectedFuelTypes.forEach(ft => {
                this.state.selectedTransmissionTypes.forEach(tt => {
                    this.wizardService
                        .getDrivenWheels(this.state.model.manufacturer, this.state.model.model, ft, tt, date)
                        .subscribe(response => {
                            this.dataLoaded = true;
                            this.allDrivenWheels = this.arrayUnique(this.allDrivenWheels.concat(response));
                        });
                });
            });
        }
    }

    handleFuelCheck(item) {
        if (this.state.selectedFuelTypes.indexOf(item) > -1) {
            this.state.selectedFuelTypes.splice(this.state.selectedFuelTypes.indexOf(item), 1);

            this.allTransmissions = [];
            this.state.selectedTransmissionTypes = [];

            this.allDrivenWheels = [];
            this.state.selectedDrivenWheels = [];

            this.dataLoaded = true;
        } else {
            this.state.selectedFuelTypes.push(item);
            if (item === 'Unleaded') {
                this.state.selectedFuelTypes.push('Premium Unleaded');
            }

            this.fetchData('transmission');
        }
    }

    handleTransmissionCheck(item) {
        if (this.state.selectedTransmissionTypes.indexOf(item) > -1) {
            this.state.selectedTransmissionTypes.splice(this.state.selectedTransmissionTypes.indexOf(item), 1);

            this.allDrivenWheels = [];
            this.state.selectedDrivenWheels = [];

            this.dataLoaded = true;
        } else {
            this.state.selectedTransmissionTypes.push(item);
            this.fetchData('drivenWheels');
        }
    }

    handleDrivenWheelsCheck(item) {
        if (this.state.selectedDrivenWheels.indexOf(item) > -1) {
            this.state.selectedDrivenWheels.splice(this.state.selectedDrivenWheels.indexOf(item), 1);

            this.dataLoaded = true;
        } else {
            this.state.selectedDrivenWheels.push(item);
        }
    }

    isTransmissionDisabled(item) {
        return this.allTransmissions.indexOf(item) <= -1;
    }

    isDrivenWheelDisabled(item) {
        return this.allDrivenWheels.indexOf(item) <= -1;
    }

    getImageUrl() {
        return 'photos/' + this.state.model.imageUrl + '.JPG';
    }

    arrayUnique(array) {
        const a = array.concat();
        for (let i = 0; i < a.length; ++i) {
            for (let j = i + 1; j < a.length; ++j) {
                if (a[i] === a[j]) {
                    a.splice(j--, 1);
                }
            }
        }
        return a;
    }
}
