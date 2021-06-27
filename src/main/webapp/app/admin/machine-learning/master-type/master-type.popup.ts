import { Component, Inject, OnInit } from '@angular/core';
import { MachineLearningService } from 'app/admin/machine-learning/machine-learning.service';
import { MAT_DIALOG_DATA, MatTableDataSource } from '@angular/material';

@Component({
    selector: 'jhi-master-type-popup',
    templateUrl: 'master-type.popup.html',
    styleUrls: ['master-type.popup.scss']
})
export class MasterTypePopupComponent implements OnInit {
    displayedColumns: string[] = ['manufacturer', 'model', 'body', 'fuelType', 'engineHp', 'engineLitres', 'registeredVehicles', 'year'];
    dataSource;

    modalData;
    masterTypes;

    constructor(private machineLearninService: MachineLearningService, @Inject(MAT_DIALOG_DATA) public data: any) {
        this.modalData = data;
    }

    ngOnInit(): void {
        this.machineLearninService.getMasterTypeData(this.modalData.manufacturer, this.modalData.model).subscribe(response => {
            console.log(response);
            this.masterTypes = response;
            this.dataSource = new MatTableDataSource(this.masterTypes);
        });
    }
}
