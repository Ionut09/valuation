import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { WizardService } from 'app/business/wizard/wizard.service';

import { FormControl } from '@angular/forms';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDatepicker } from '@angular/material/datepicker';

import * as moment from 'moment';
import { Moment } from 'moment';
import { MomentDateAdapter } from '@angular/material-moment-adapter';

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
    selector: 'jhi-car-manufacturer',
    templateUrl: './manufacturer.component.html',
    styleUrls: ['manufacturer.component.scss'],
    providers: [
        // `MomentDateAdapter` can be automatically provided by importing `MomentDateModule` in your
        // application's root module. We provide it at the component level here, due to limitations of
        // our example generation script.
        { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE] },

        { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS }
    ]
})
export class CarManufacturerComponent implements OnInit {
    @Input() state: any;
    @Output() manufacturerSelected = new EventEmitter();

    manufacturingDateControl = new FormControl(moment());
    registrationDateControl = new FormControl(moment());
    manufacturerControl = new FormControl();

    manufacturers: Array<String>;
    filteredManufacturers: Observable<Array<String>>;

    constructor(private wizardService: WizardService) {}

    ngOnInit(): void {
        this.wizardService.getManufacturers().subscribe(response => {
            this.manufacturers = response.map(item => item.name);

            this.filteredManufacturers = this.manufacturerControl.valueChanges.pipe(
                startWith<string | String>(''),
                map(manufacturer => (manufacturer ? this._filter(manufacturer) : this.manufacturers.slice()))
            );
        });
    }

    private _filter(name): Array<String> {
        const filterValue = name.toLowerCase();

        return this.manufacturers.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
    }

    displayFn(manufacturer?: string): string | undefined {
        return manufacturer ? manufacturer : undefined;
    }

    emitDataUpdateEvent() {
        if (this.manufacturingDateControl && this.registrationDateControl) {
            this.manufacturerSelected.emit({
                manufacturer: this.manufacturerControl.value,
                manufacturingYear: this.manufacturingDateControl.value.year(),
                manufacturingDate: moment(this.manufacturingDateControl.value).format('YYYY-MM-DD'),
                registrationYear: this.registrationDateControl.value.year(),
                registrationDate: moment(this.registrationDateControl.value).format('YYYY-MM-DD')
            });
        } else if (this.manufacturingDateControl) {
            this.manufacturerSelected.emit({
                manufacturer: this.manufacturerControl.value,
                manufacturingYear: this.manufacturingDateControl.value.year(),
                manufacturingDate: moment(this.manufacturingDateControl.value).format('YYYY-MM-DD')
            });
        } else if (this.registrationDateControl) {
            this.manufacturerSelected.emit({
                manufacturer: this.manufacturerControl.value,
                registrationYear: this.registrationDateControl.value.year(),
                registrationDate: moment(this.registrationDateControl.value).format('YYYY-MM-DD')
            });
        } else {
            this.manufacturerSelected.emit({ manufacturer: this.manufacturerControl.value });
        }
    }

    chosenManufacturingYearHandler(normalizedYear: Moment) {
        const ctrlValue = this.manufacturingDateControl.value;
        ctrlValue.year(normalizedYear.year());
        this.manufacturingDateControl.setValue(ctrlValue);

        this.emitDataUpdateEvent();
    }
    chosenManufacturingMonthHandler(normalizedMonth: Moment, datepicker: MatDatepicker<Moment>) {
        const ctrlValue = this.manufacturingDateControl.value;
        ctrlValue.month(normalizedMonth.month());
        this.manufacturingDateControl.setValue(ctrlValue);
        datepicker.close();

        this.emitDataUpdateEvent();
    }

    chosenRegistrationYearHandler(normalizedYear: Moment) {
        const ctrlValue = this.registrationDateControl.value;
        ctrlValue.year(normalizedYear.year());
        this.registrationDateControl.setValue(ctrlValue);

        this.emitDataUpdateEvent();
    }
    chosenRegistrationMonthHandler(normalizedMonth: Moment, datepicker: MatDatepicker<Moment>) {
        const ctrlValue = this.registrationDateControl.value;
        ctrlValue.month(normalizedMonth.month());
        this.registrationDateControl.setValue(ctrlValue);
        datepicker.close();

        this.emitDataUpdateEvent();
    }

    chosenManufacturer() {
        this.emitDataUpdateEvent();
    }
}
