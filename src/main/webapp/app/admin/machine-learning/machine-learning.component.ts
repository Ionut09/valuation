import { Component, OnInit } from '@angular/core';
import { MachineLearningService } from 'app/admin/machine-learning/machine-learning.service';
import { MatDialog } from '@angular/material';
import { MasterTypePopupComponent } from 'app/admin/machine-learning/master-type/master-type.popup';
import { MachineLearningManagementComponent } from 'app/admin/machine-learning/management/machine-learning-management.component';

@Component({
    selector: 'jhi-machine-learning',
    templateUrl: './machine-learning.component.html',
    styleUrls: ['machine-learning.component.scss']
})
export class MachineLearningComponent implements OnInit {
    manufacturers;
    selectedManufacturer;

    models;
    selectedModel;

    dataLoaded = false;
    data;

    trainingInProgress = false;

    rmseDescription =
        'Root Mean Square Error (RMSE) is the standard deviation of the residuals (prediction errors). ' +
        'Residuals are a measure of how far from the regression line data points are; ' +
        'RMSE is a measure of how spread out these residuals are. In other words, it tells you how concentrated the data is around the line of best fit.';
    varianceScore =
        'r2 score—varies between 0 and 100%. It is closely related to the RMSE (see above), but not the same. ' +
        'So if it is 100%, the two variables are perfectly correlated, i.e., with no variance at all. A low value would show a low level of correlation, ' +
        'meaning a regression model that is not valid, but not in all cases.';

    constructor(private machineLearningService: MachineLearningService, private dialog: MatDialog) {}

    ngOnInit(): void {
        this.machineLearningService.getLock().subscribe(respone => (this.trainingInProgress = <any>respone));

        this.machineLearningService.getManufacturers().subscribe(response => {
            this.manufacturers = response;
        });
    }

    handleManufacturerSelection(event) {
        this.selectedManufacturer = event;

        this.machineLearningService.getModels(this.selectedManufacturer).subscribe(response => {
            this.models = response;
        });
    }

    handleModelSelection(event) {
        this.selectedModel = event;

        this.machineLearningService.getTrainingResults(this.selectedManufacturer, this.selectedModel).subscribe(response => {
            console.log(response);
            this.dataLoaded = true;
            this.data = response;
        });
    }

    openMasterTypeDialog() {
        const dialogRef = this.dialog.open(MasterTypePopupComponent, {
            data: {
                manufacturer: this.selectedManufacturer,
                model: this.selectedModel
            }
        });
    }

    openMLManagementDialog() {
        const dialogRef = this.dialog.open(MachineLearningManagementComponent, {
            width: '640px',
            data: {
                manufacturer: this.selectedManufacturer,
                model: this.selectedModel
            }
        });

        dialogRef.afterClosed().subscribe(success => {
            setTimeout(() => {
                this.machineLearningService.getLock().subscribe(respone => (this.trainingInProgress = <any>respone));
            }, 2000);
        });
    }
}
