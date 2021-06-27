import { Component, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA } from '@angular/material';

@Component({
    selector: 'jhi-options-update-snackbar',
    templateUrl: 'options-update-snackbar.component.html',
    styles: []
})
export class OptionsUpdateSnackbarComponent {
    price: number;

    constructor(@Inject(MAT_SNACK_BAR_DATA) public data: any) {
        this.price = data.price;
    }
}
