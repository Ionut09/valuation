import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDatepicker } from '@angular/material/datepicker';

import * as moment from 'moment';
import { Moment } from 'moment';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { MatDialogRef } from '@angular/material';

export const MY_FORMATS = {
    parse: {
        dateInput: 'MM/YYYY'
    },
    display: {
        dateInput: 'MM/YYYY',
        monthYearLabel: 'MMM YYYY',
        dateA11yLabel: 'LL',
        monthYearA11yLabel: 'MMMM YYYY'
    }
};

@Component({
    selector: 'jhi-registration-year-popup',
    templateUrl: 'registration-year.popup.html',
    styleUrls: ['registration-year.popup.scss'],
    providers: [
        // `MomentDateAdapter` can be automatically provided by importing `MomentDateModule` in your
        // application's root module. We provide it at the component level here, due to limitations of
        // our example generation script.
        { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE] },

        { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS }
    ]
})
export class RegistrationYearPopupComponent {
    registrationDateControl;

    constructor(private dialogRef: MatDialogRef<RegistrationYearPopupComponent>) {
        this.registrationDateControl = new FormControl(moment(), []);
        this.registrationDateControl.clearValidators();
    }

    chosenRegistrationYearHandler(normalizedYear: Moment) {
        const ctrlValue = this.registrationDateControl.value;
        ctrlValue.year(normalizedYear.year());
        this.registrationDateControl.setValue(ctrlValue);
    }

    chosenRegistrationMonthHandler(normalizedMonth: Moment, datepicker: MatDatepicker<Moment>) {
        const ctrlValue = this.registrationDateControl.value;
        ctrlValue.month(normalizedMonth.month());
        this.registrationDateControl.setValue(ctrlValue);

        datepicker.close();
    }

    handleCloseButton() {
        this.dialogRef.close({ registration: moment(this.registrationDateControl.value).format('YYYY-MM-DD') });
    }
}
