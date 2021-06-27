import { Component, EventEmitter, Output } from '@angular/core';
import { WizardService } from 'app/business/wizard/wizard.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { MatDialog } from '@angular/material';
import { RegistrationYearPopupComponent } from 'app/business/wizard/vin/registration-popup/registration-year.popup';

@Component({
    selector: 'jhi-wizard-vin',
    templateUrl: './vin.component.html',
    styleUrls: ['vin.component.scss']
})
export class WizardVinComponent {
    @Output() manualSearch = new EventEmitter();
    @Output() vinEvent = new EventEmitter();

    @BlockUI() blockUI: NgBlockUI;

    vin = '';
    invalidVin = false;
    errorVin = false;

    constructor(private wizardService: WizardService, private dialog: MatDialog) {}

    handleManualSearch() {
        this.manualSearch.emit();
    }

    handleVinQuery() {
        if (!(this.vin.length === 17)) {
            this.invalidVin = true;
            return;
        }

        this.blockUI.start('Se procesează seria de șasiu, procesul poate dura puțin...');
        this.wizardService.queryVin(this.vin.toUpperCase()).subscribe(
            response => {
                this.blockUI.stop();
                const dialogRef = this.dialog.open(RegistrationYearPopupComponent, { disableClose: true });

                dialogRef.afterClosed().subscribe(result => {
                    response['registration'] = result.registration;
                    this.vinEvent.emit(response);
                });
            },

            error => {
                this.blockUI.stop();
                this.errorVin = true;
            }
        );
    }

    checkProperties(obj) {
        for (const key in obj) {
            if (obj[key] === null || obj[key] === '') {
                console.error('Value for key not found!: ' + key);
                return true;
            }
        }

        return false;
    }
}
