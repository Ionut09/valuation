import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { WizardService } from 'app/business/wizard/wizard.service';
import { MatDialog, MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { Vehicle } from 'app/business/model/wizard/Vehicle';
import { VersionConfirmationPopupComponent } from 'app/business/wizard/stepper/version/confirmation/version-confirmation.popup';
import * as moment from 'moment';
import { JhiLanguageService } from 'ng-jhipster';

@Component({
    selector: 'jhi-car-version',
    templateUrl: './version.component.html',
    styleUrls: ['version.component.scss']
})
export class CarVersionComponent {
    displayedColumns: string[] = [
        'Make',
        'Model',
        'Version',
        'Trim',
        'Model Year',
        'Fuel Type',
        'Transmission',
        'Power',
        'KW',
        'CMC',
        'Doors',
        'Start Selling',
        'End Selling',
        'Price'
    ];
    dataSource: MatTableDataSource<Vehicle>;

    @Input() state;
    @Output() carSelected = new EventEmitter();

    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;

    dataLoaded = false;

    distinctFuels;
    distinctTransmissions;
    distinctDrivenWheels;

    activeFuelFilters = [];
    activeTransmissionFilters = [];
    activeDrivenWheelsFilters = [];

    originalData: Array<Vehicle>;

    fromVin = false;
    extraOptionals = [];

    constructor(private wizardService: WizardService, private languageService: JhiLanguageService, private dialog: MatDialog) {}

    handleFuelCheck(data) {
        if (this.activeFuelFilters.includes(data)) {
            this.activeFuelFilters.splice(this.activeFuelFilters.indexOf(data), 1);
        } else {
            this.activeFuelFilters.push(data);
        }

        this.applyFilters();
    }

    handleTransmissionCheck(data) {
        if (this.activeTransmissionFilters.includes(data)) {
            this.activeTransmissionFilters.splice(this.activeTransmissionFilters.indexOf(data), 1);
        } else {
            this.activeTransmissionFilters.push(data);
        }

        this.applyFilters();
    }

    handleDrivenWheelsCheck(data) {
        if (this.activeDrivenWheelsFilters.includes(data)) {
            this.activeDrivenWheelsFilters.splice(this.activeDrivenWheelsFilters.indexOf(data), 1);
        } else {
            this.activeDrivenWheelsFilters.push(data);
        }

        this.applyFilters();
    }

    applyFilters() {
        const filtered = this.originalData.filter(el => {
            let found = false;

            if (this.activeFuelFilters.length > 0) {
                if (this.activeFuelFilters.indexOf(el.fuelType) > -1) {
                    found = true;
                } else {
                    found = false;

                    return found;
                }
            }

            if (this.activeDrivenWheelsFilters.length > 0) {
                if (this.activeDrivenWheelsFilters.indexOf(el.drivenWheels) > -1) {
                    found = true;
                } else {
                    found = false;

                    return found;
                }
            }

            if (this.activeTransmissionFilters.length > 0) {
                if (this.activeTransmissionFilters.indexOf(el.transmissionDescription) > -1) {
                    found = true;
                } else {
                    found = false;

                    return found;
                }
            }

            return found;
        });

        if (filtered.length > 0) {
            this.dataSource = new MatTableDataSource(filtered);
        } else if (
            this.activeFuelFilters.length > 0 ||
            this.activeDrivenWheelsFilters.length > 0 ||
            this.activeTransmissionFilters.length > 0
        ) {
            this.dataSource = new MatTableDataSource([]);
        } else {
            this.dataSource = new MatTableDataSource(this.originalData);
        }

        setTimeout(() => {
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
            this.dataSource.sortingDataAccessor = (data, header) => data[header];
        });
    }

    applyFilter(filterValue: string) {
        this.dataSource.filter = filterValue.trim().toLowerCase();
    }

    fetchData() {
        this.dataSource = new MatTableDataSource([]);
        let date;

        if (moment(this.state.manufacturingDate).year() === moment(this.state.registrationDate).year()) {
            date = this.state.registrationDate;
        } else {
            date = this.state.manufacturingDate;
        }

        this.wizardService
            .getVehicles(
                this.state.model.manufacturer,
                this.state.model.model,
                this.state.body.body,
                this.state.body.doorsNo,
                this.state.generation.generation,
                date,
                this.state.selectedFuelTypes,
                this.state.selectedTransmissionTypes,
                this.state.selectedDrivenWheels
            )
            .subscribe(
                response => {
                    console.log(response);

                    this.dataLoaded = true;
                    this.dataSource = new MatTableDataSource(response);
                    this.originalData = response;

                    this.distinctFuels = Array.from(new Set(response.map(x => x.fuelType)));
                    this.distinctTransmissions = Array.from(new Set(response.map(x => x.transmissionDescription)));
                    this.distinctDrivenWheels = Array.from(new Set(response.map(x => x.drivenWheels)));

                    setTimeout(() => {
                        this.dataSource.paginator = this.paginator;
                        this.dataSource.sort = this.sort;
                        this.dataSource.sortingDataAccessor = (data, header) => data[header];
                    });
                },
                error => (this.dataLoaded = false)
            );
    }

    fetchDataFromVIN(data) {
        this.dataSource = new MatTableDataSource([]);
        this.extraOptionals = data.extraOptionCodes;

        this.state.manufacturingDate = data.date;
        this.state.registrationDate = data.registration;

        console.log(data);

        this.wizardService
            .getVehiclesVinQuery(
                data.manufacturer,
                data.model,
                data.body,
                data.doorsNo,
                data.date,
                data.registration,
                data.fuelType,
                data.horsePower,
                data.engineKw,
                data.gearbox,
                data.cc
            )
            .subscribe(
                response => {
                    console.log(response);

                    this.fromVin = true;

                    this.dataLoaded = true;
                    this.dataSource = new MatTableDataSource(response);
                    this.originalData = response;

                    this.distinctFuels = Array.from(new Set(response.map(x => x.fuelType)));
                    this.distinctTransmissions = Array.from(new Set(response.map(x => x.transmissionDescription)));
                    this.distinctDrivenWheels = Array.from(new Set(response.map(x => x.drivenWheels)));

                    setTimeout(() => {
                        this.dataSource.paginator = this.paginator;
                        this.dataSource.sort = this.sort;
                        this.dataSource.sortingDataAccessor = (_data, header) => _data[header];
                    });
                },
                error => (this.dataLoaded = false)
            );
    }

    handleCarSelection(row) {
        this.openConfirmation(row);
    }

    openConfirmation(row) {
        const dialogRef = this.dialog.open(VersionConfirmationPopupComponent, {
            data: row
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result === true) {
                const payload = {
                    car: row,
                    extraOptionCodes: this.extraOptionals
                };

                this.carSelected.emit(payload);
            }
        });
    }

    parseDate(date: string) {
        return moment(date, 'YYYYMMDD').format('DD/MM/YYYY');
    }

    formatUpperLetter(string, element) {
        if (string == null) {
            return element.transmission;
        }

        return string.charAt(0).toUpperCase() + string.slice(1);
    }

    formatTransmissionText(value: String) {
        if (value === 'M') {
            return 'Manual';
        } else if (value === 'A') {
            return 'Automatic';
        } else {
            return value;
        }
    }
}
