import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { OptionsService } from 'app/business/wizard/options/options.service';
import { StandardOption } from 'app/business/model/options/StandardOption';
import { ExtraOption } from 'app/business/model/options/ExtraOption';
import { JhiLanguageService } from 'ng-jhipster';
import { MatDialog, MatSnackBar } from '@angular/material';
import { OptionsUpdateSnackbarComponent } from 'app/business/wizard/options/snackbar/options-update-snackbar.component';
import { OptionsConfirmationPopupComponent } from 'app/business/wizard/options/confirmation/options-confirmation.popup';
import { Body } from 'app/business/model/wizard/Body';
import { Router } from '@angular/router';
import { BlockUI, NgBlockUI } from 'ng-block-ui';

@Component({
    selector: 'jhi-wizard-options',
    templateUrl: './options.component.html',
    styleUrls: ['options.component.scss']
})
export class WizardOptionsComponent implements OnInit {
    @Input() state: any;

    @BlockUI() blockUI: NgBlockUI;

    stdOptionsFormGroup: FormGroup;
    extraOptionsFormGroup: FormGroup;
    standardOptions: Array<StandardOption>;
    extraOptions: Array<ExtraOption>;

    displayError = false;

    extraOptionsValue = 0;
    numberOfKms = 0;
    tiresAttrition = 0;
    gpl = false;
    loss = 0;

    stdDataLoaded = false;
    extraDataLoaded = false;

    invalidKmNo = false;
    vinExtraOptionals = [];

    constructor(
        private optionsService: OptionsService,
        private formBuilder: FormBuilder,
        private languageService: JhiLanguageService,
        private snackBar: MatSnackBar,
        private dialog: MatDialog,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.stdOptionsFormGroup = this.formBuilder.group({
            standardOptions: this.formBuilder.array([])
        });
        this.extraOptionsFormGroup = this.formBuilder.group({
            extraOptions: this.formBuilder.array([])
        });
    }

    fetchData(extraOptionals) {
        const vehicleId = this.state.car.vehicleId;

        this.languageService.getCurrent().then(lang => console.log(lang));

        if (vehicleId) {
            this.optionsService.getStandardOptions(vehicleId).subscribe(standardOptions => {
                this.languageService.getCurrent().then(lang => {
                    this.stdDataLoaded = true;
                    if (lang === 'ro') {
                        this.standardOptions = standardOptions.filter(element => element.languageId === 55);
                    } else {
                        this.standardOptions = standardOptions.filter(element => element.languageId === 19);
                    }
                });
            });

            this.optionsService.getExtraOptions(vehicleId).subscribe(extraOptions => {
                this.extraDataLoaded = true;
                this.extraOptions = extraOptions;

                setTimeout(() => {
                    this.vinExtraOptionals = extraOptionals;

                    this.vinExtraOptionals.forEach(item => {
                        this.extraOptions.forEach(option => {
                            if (item === option.optionCode) {
                                this.addSelectedExtraOptions(option);
                            }
                        });
                    });
                });
            });
        }
    }

    onExtraChange(event, extraOption) {
        const _extraOptions = (<FormArray>this.extraOptionsFormGroup.get('extraOptions')) as FormArray;

        if (event.checked) {
            _extraOptions.push(new FormControl(event.source.value));
            this.extraOptionsValue += event.source.value.price;
            extraOption.checked = true;

            this.optionsService.checkInclusion(event.source.value.optionId, this.state.car.vehicleId).subscribe(success => {
                const ids = this.getOptionIds(success.rule);
                ids.forEach(id => {
                    const toBeAdded = this.extraOptions.filter(element => element.optionId === +id);
                    if (toBeAdded.length > 0) {
                        this.computeInclusion(toBeAdded[0]);
                    }
                }, this);
            });

            this.optionsService.checkExclusion(event.source.value.optionId, this.state.car.vehicleId).subscribe(success => {
                const ids = this.getOptionIds(success.rule);

                ids.forEach(id => {
                    const toBeAdded = this.extraOptions.filter(element => element.optionId === +id);
                    if (toBeAdded.length > 0) {
                        this.computeExclusion(toBeAdded[0]);
                    }
                }, this);
            });
        } else {
            // check if it has exclusions to re-enable
            const i = _extraOptions.controls.findIndex(x => x.value === event.source.value);
            _extraOptions.removeAt(i);
            this.extraOptionsValue -= event.source.value.price;
            extraOption.checked = false;

            this.optionsService.checkInclusion(event.source.value.optionId, this.state.car.vehicleId).subscribe(success => {
                const ids = this.getOptionIds(success.rule);
                ids.forEach(id => {
                    const toBeAdded = this.extraOptions.filter(element => element.optionId === +id);
                    if (toBeAdded.length > 0) {
                        this.addSelectedExtraWithEnable(toBeAdded[0]);
                    }
                }, this);
            });

            this.optionsService.checkExclusion(event.source.value.optionId, this.state.car.vehicleId).subscribe(success => {
                const ids = this.getOptionIds(success.rule);
                ids.forEach(id => {
                    const toBeAdded = this.extraOptions.filter(element => element.optionId === +id);
                    if (toBeAdded.length > 0) {
                        this.addSelectedExtraWithDisable(toBeAdded[0]);
                    }
                }, this);
            });
        }

        this.openSnackBar();
    }

    addSelectedExtraOptions(extraOption) {
        console.log('Adding: ');
        console.log(extraOption);

        const extraOptions = (<FormArray>this.extraOptionsFormGroup.get('extraOptions')) as FormArray;

        extraOptions.push(new FormControl(extraOption));
        this.extraOptionsValue += extraOption.price;

        extraOption.checked = true;
    }

    computeInclusion(extraOption) {
        extraOption.disabled = true;
        extraOption.included = true;

        if (extraOption.checked) {
            this.extraOptionsValue -= extraOption.price;
            extraOption.checked = false;
        }
    }

    computeExclusion(extraOption) {
        extraOption.disabled = true;
        extraOption.excluded = true;

        if (extraOption.checked) {
            this.extraOptionsValue -= extraOption.price;
            extraOption.checked = false;
        }
    }

    addSelectedExtraWithEnable(extraOption) {
        extraOption.disabled = false;
        extraOption.included = false;
    }

    addSelectedExtraWithDisable(extraOption) {
        extraOption.disabled = false;
        extraOption.excluded = false;
    }

    openSnackBar() {
        this.snackBar.openFromComponent(OptionsUpdateSnackbarComponent, {
            duration: 5 * 1000,
            data: { price: this.state.car.basePrice * 1.19 + this.extraOptionsValue * 1.19 }
        });
    }

    openConfirmation() {
        if (this.numberOfKms < 3) {
            this.invalidKmNo = true;
            return;
        }

        const dialogRef = this.dialog.open(OptionsConfirmationPopupComponent);

        dialogRef.afterClosed().subscribe(result => {
            if (result === true) {
                const extraOptions: Array<ExtraOption> = new Array<ExtraOption>();
                ((<FormArray>this.extraOptionsFormGroup.get('extraOptions')) as FormArray).controls.forEach(element => {
                    extraOptions.push(element.value);
                });

                const dto = {
                    vehicleId: this.state.car.vehicleId,
                    numberOfKilometers: this.numberOfKms,
                    attritionValue: -Math.abs(this.tiresAttrition),
                    bodyDamageValue: -Math.abs(this.loss),
                    gpl: this.gpl,
                    fabricationDate: this.state.manufacturingDate,
                    registrationDate: this.state.registrationDate,
                    extraOptionsPrice: this.extraOptionsValue,
                    priceAsNew: this.state.car.basePrice,
                    options: extraOptions
                };

                this.blockUI.start('Se creează evaluarea (procesul poate dura puțin în funcție de mașina aleasă)...'); // Start blocking

                this.optionsService.createValuation(dto).subscribe(
                    response => {
                        console.log(response);
                        this.blockUI.stop();
                        this.router.navigate(['/valuation', (<any>response).id]);
                    },
                    error => {
                        this.blockUI.stop();
                        this.displayError = true;
                    }
                );
            }
        });
    }

    getImageUrl(body: Body) {
        return 'photos/' + body.imageUrl + '.JPG';
    }

    getOptionIds(str: string) {
        const found = [],
            rxp = /{([^}]+)}/g;
        let curMatch;

        while ((curMatch = rxp.exec(str))) {
            found.push(curMatch[1]);
        }

        return found;
    }
}
