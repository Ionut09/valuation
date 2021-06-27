import { Component, EventEmitter, Input, Output } from '@angular/core';
import { WizardService } from 'app/business/wizard/wizard.service';
import { Generation } from 'app/business/model/wizard/Generation';
import { Body } from 'app/business/model/wizard/Body';
import * as moment from 'moment';

@Component({
    selector: 'jhi-car-generation',
    templateUrl: './generation.component.html',
    styleUrls: ['generation.component.scss']
})
export class CarGenerationComponent {
    @Input() state: any;
    @Output() generationSelected = new EventEmitter();

    generations: Array<Generation> = [];
    dataLoaded = false;

    constructor(private wizardService: WizardService) {}

    fetchData() {
        this.dataLoaded = false;
        this.generations = [];

        let date;

        if (moment(this.state.manufacturingDate).year() === moment(this.state.registrationDate).year()) {
            date = this.state.registrationDate;
        } else {
            date = this.state.manufacturingDate;
        }

        this.wizardService
            .getGenerations(
                this.state.model.manufacturer,
                this.state.model.model,
                this.state.body.body,
                this.state.body.doorsNo,
                date,
                this.state.selectedFuelTypes,
                this.state.selectedTransmissionTypes,
                this.state.selectedDrivenWheels
            )
            .subscribe(
                response => {
                    this.dataLoaded = true;
                    this.generations = response;
                },
                error => (this.dataLoaded = false)
            );
    }

    handleGenerationSelection(generation: Generation) {
        this.state.generation = generation;
        this.generationSelected.emit();
    }

    getImageUrl(body: Body) {
        return 'photos/' + body.imageUrl + '.JPG';
    }
}
