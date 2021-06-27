import { Component, OnInit } from '@angular/core';
import { AppConfigurationService } from './app-configuration.service';
import { MatSnackBar } from '@angular/material';

@Component({
    selector: 'jhi-app-configuration',
    templateUrl: './app-configuration.component.html',
    styleUrls: ['app-configuration.component.scss']
})
export class AppConfigurationComponent implements OnInit {
    carSegmentsDepreciations;

    loadingData = false;

    constructor(private appConfigurationService: AppConfigurationService, private _snackBar: MatSnackBar) {}

    ngOnInit(): void {
        this.appConfigurationService.getCarSegments().subscribe(response => {
            console.log(response);

            this.carSegmentsDepreciations = response;
        });
    }

    handleSaveButton() {
        this.loadingData = true;
        this.appConfigurationService.updateCarSegments(this.carSegmentsDepreciations).subscribe(response => {
            console.log(response);

            this.carSegmentsDepreciations = response;

            this.openSnackBar('Settings successfully saved!', 'Close');
            this.loadingData = false;
        });
    }

    openSnackBar(message: string, action: string) {
        this._snackBar.open(message, action, {
            duration: 6000
        });
    }
}
