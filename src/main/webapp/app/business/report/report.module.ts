import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { ValuationSharedModule } from 'app/shared';
import { BlockUIModule } from 'ng-block-ui';
import { RouterModule } from '@angular/router';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { ReportComponent } from 'app/business/report/report.component';
import { REPORT_ROUTE } from 'app/business/report/report.route';
import { ReportService } from 'app/business/report/report.service';

@NgModule({
    imports: [ValuationSharedModule, BlockUIModule.forRoot(), RouterModule.forChild([REPORT_ROUTE]), NgxChartsModule],
    declarations: [ReportComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }, ReportService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReportModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
