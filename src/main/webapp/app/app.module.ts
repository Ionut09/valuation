import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage } from 'ngx-webstorage';
import { NgJhipsterModule } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { ValuationSharedModule } from 'app/shared';
import { ValuationCoreModule } from 'app/core';
import { ValuationAppRoutingModule } from './app-routing.module';
import { ValuationHomeModule } from 'app/home';
import { ValuationAccountModule } from './account/account.module';
import { ValuationEntityModule } from './entities/entity.module';
import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { ActiveMenuDirective, ErrorComponent, FooterComponent, JhiMainComponent, NavbarComponent, PageRibbonComponent } from './layouts';

import { ValuationInfoModule } from 'app/business/valuation/valuation.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { WizardModule } from 'app/business/wizard/wizard.module';
import { ReportModule } from 'app/business/report/report.module';

@NgModule({
    imports: [
        BrowserModule,
        FontAwesomeModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        NgJhipsterModule.forRoot({
            // set below to true to make alerts look like toast
            alertAsToast: false,
            alertTimeout: 5000,
            i18nEnabled: true,
            defaultI18nLang: 'ro'
        }),
        BrowserAnimationsModule,
        ValuationSharedModule.forRoot(),
        HttpClientModule,
        ValuationCoreModule,
        ValuationHomeModule,
        ValuationAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        ValuationEntityModule,
        // business-related modules
        ValuationInfoModule,
        WizardModule,
        ReportModule,
        // always the last one, check: https://stackoverflow.com/a/49804227/5242344
        ValuationAppRoutingModule
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class ValuationAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
