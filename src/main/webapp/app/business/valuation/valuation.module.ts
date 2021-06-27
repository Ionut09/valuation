import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ValuationSharedModule } from 'app/shared';

import { ValuationInfoComponent } from 'app/business/valuation/valuation.component';
import { VALUATION_INFO_ROUTE } from 'app/business/valuation/valuation.route';
import { ValuationService } from 'app/business/valuation/valuation.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { BlockUIModule } from 'ng-block-ui';
import { ChartistModule } from 'ng-chartist';

@NgModule({
    imports: [ValuationSharedModule, ChartistModule, BlockUIModule.forRoot(), RouterModule.forChild([VALUATION_INFO_ROUTE])],
    declarations: [ValuationInfoComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }, ValuationService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ValuationInfoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
