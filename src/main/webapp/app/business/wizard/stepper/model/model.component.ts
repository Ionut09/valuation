import { Component, EventEmitter, Input, Output } from '@angular/core';
import { WizardService } from 'app/business/wizard/wizard.service';
import { Model } from 'app/business/model/wizard/Model';

@Component({
    selector: 'jhi-car-model',
    templateUrl: './model.component.html',
    styleUrls: ['model.component.scss']
})
export class CarModelComponent {
    @Input() state: any;
    @Output() modelSelected = new EventEmitter();

    models: Array<Model> = [];
    dataLoaded = false;

    constructor(private wizardService: WizardService) {}

    fetchData() {
        this.models = [];
        this.dataLoaded = false;

        let date;

        // if (moment(this.state.manufacturingDate).year() === moment(this.state.registrationDate).year()) {
        date = this.state.registrationDate;
        // } else {
        //     date = this.state.manufacturingDate;
        // }

        if (this.state.manufacturer.length > 0) {
            if (this.state.manufacturingYear && this.state.registrationYear) {
                this.wizardService.getModelsIn(this.state.manufacturer, date).subscribe(response => {
                    this.dataLoaded = true;
                    this.models = response;
                });
            } else {
                this.wizardService.getModelsOf(this.state.manufacturer).subscribe(response => {
                    this.dataLoaded = true;
                    this.models = response;
                });
            }
        }
    }

    getImageUrl(model: Model) {
        return 'photos/' + model.imageUrl + '.JPG';
    }

    handleModelSelection(model: Model) {
        this.state.model = model;
        this.modelSelected.emit();
    }
}
