import { Component, OnInit } from '@angular/core';
import { MachineLearningService } from 'app/admin/machine-learning/machine-learning.service';
import { MatDialogRef } from '@angular/material';
import { Router } from '@angular/router';

@Component({
    selector: 'jhi-machine-learning-popup',
    templateUrl: 'machine-learning-management.component.html',
    styleUrls: ['machine-learning-management.component.scss']
})
export class MachineLearningManagementComponent implements OnInit {
    trainingInProgress = false;

    constructor(
        private machineLearningService: MachineLearningService,
        private dialogRef: MatDialogRef<MachineLearningManagementComponent>,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.machineLearningService.getLock().subscribe(respone => (this.trainingInProgress = <any>respone));
    }

    handleTriggerClick() {
        this.machineLearningService
            .triggerTraining()
            .subscribe(response => console.log('ML Training triggered!'), error => console.error('Failed trigger for ML training!'));

        this.closeDialog();
    }

    closeDialog() {
        this.dialogRef.close();
        this.router.navigate(['/']);
    }
}
