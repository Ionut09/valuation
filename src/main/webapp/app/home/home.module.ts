import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ValuationSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { HomeService } from 'app/home/home.service';

@NgModule({
    imports: [ValuationSharedModule, FormsModule, ReactiveFormsModule, RouterModule.forChild([HOME_ROUTE])],
    declarations: [HomeComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }, HomeService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ValuationHomeModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
