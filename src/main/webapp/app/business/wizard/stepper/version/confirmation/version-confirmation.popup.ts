import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
    selector: 'jhi-version-confirmation-popup',
    templateUrl: 'version-confirmation.popup.html',
    styleUrls: ['version-confirmation.popup.scss']
})
export class VersionConfirmationPopupComponent {
    state: any;

    constructor(@Inject(MAT_DIALOG_DATA) public data) {
        this.state = data;
        console.log(this.state);
    }
}
