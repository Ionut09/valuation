import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ValuationSharedModule } from 'app/shared';
import { WizardComponent } from 'app/business/wizard/wizard.component';
import { WIZARD_ROUTE } from 'app/business/wizard/wizard.route';
import { WizardOptionsComponent } from 'app/business/wizard/options/options.component';
import { WizardVinComponent } from 'app/business/wizard/vin/vin.component';
import { CarBodyComponent } from 'app/business/wizard/stepper/body/body.component';
import { CarGenerationComponent } from 'app/business/wizard/stepper/generation/generation.component';
import { CarManufacturerComponent } from 'app/business/wizard/stepper/manufacturer/manufacturer.component';
import { CarModelComponent } from 'app/business/wizard/stepper/model/model.component';
import { CarVersionComponent } from 'app/business/wizard/stepper/version/version.component';
import { WizardStepperComponent } from 'app/business/wizard/stepper/stepper.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FuelTractionComponent } from 'app/business/wizard/stepper/fuel/fuel-traction.component';
import { WizardService } from 'app/business/wizard/wizard.service';
import { FlexLayoutModule } from '@angular/flex-layout';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { VersionConfirmationPopupComponent } from 'app/business/wizard/stepper/version/confirmation/version-confirmation.popup';
import { OptionsService } from 'app/business/wizard/options/options.service';
import { OptionsUpdateSnackbarComponent } from 'app/business/wizard/options/snackbar/options-update-snackbar.component';
import { OptionsConfirmationPopupComponent } from 'app/business/wizard/options/confirmation/options-confirmation.popup';
import { BlockUIModule } from 'ng-block-ui';
import { RegistrationYearPopupComponent } from 'app/business/wizard/vin/registration-popup/registration-year.popup';

@NgModule({
    imports: [
        ValuationSharedModule,
        FormsModule,
        ReactiveFormsModule,
        FlexLayoutModule,
        RouterModule.forChild([WIZARD_ROUTE]),
        BlockUIModule.forRoot()
    ],
    declarations: [
        WizardComponent,
        WizardOptionsComponent,
        WizardVinComponent,
        CarBodyComponent,
        CarGenerationComponent,
        CarManufacturerComponent,
        CarModelComponent,
        CarVersionComponent,
        FuelTractionComponent,
        WizardStepperComponent,
        VersionConfirmationPopupComponent,
        OptionsUpdateSnackbarComponent,
        OptionsConfirmationPopupComponent,
        RegistrationYearPopupComponent
    ],
    entryComponents: [
        VersionConfirmationPopupComponent,
        OptionsUpdateSnackbarComponent,
        OptionsConfirmationPopupComponent,
        RegistrationYearPopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }, WizardService, OptionsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WizardModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
