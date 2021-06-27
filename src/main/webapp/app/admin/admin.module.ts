import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { ValuationSharedModule } from 'app/shared';
import {
    adminState,
    AuditsComponent,
    DataIngestComponent,
    JhiConfigurationComponent,
    JhiDocsComponent,
    JhiHealthCheckComponent,
    JhiHealthModalComponent,
    JhiMetricsMonitoringComponent,
    UserMgmtComponent,
    UserMgmtDeleteDialogComponent,
    UserMgmtDetailComponent,
    UserMgmtUpdateComponent
} from './';
import { DataIngestProgressComponent } from 'app/admin/data-ingest/progress/data-ingest-progress.component';
import { DataIngestUploadComponent } from 'app/admin/data-ingest/file-upload/data-ingest-upload.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MachineLearningComponent } from 'app/admin/machine-learning/machine-learning.component';
import { MasterTypePopupComponent } from 'app/admin/machine-learning/master-type/master-type.popup';
import { AppConfigurationComponent } from 'app/admin/app-configuration/app-configuration.component';
import { MachineLearningManagementComponent } from 'app/admin/machine-learning/management/machine-learning-management.component';

/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

@NgModule({
    imports: [
        ValuationSharedModule,
        ReactiveFormsModule,
        RouterModule.forChild(adminState)
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    declarations: [
        AuditsComponent,
        UserMgmtComponent,
        UserMgmtDetailComponent,
        UserMgmtUpdateComponent,
        UserMgmtDeleteDialogComponent,
        DataIngestProgressComponent,
        DataIngestUploadComponent,
        DataIngestComponent,
        MachineLearningComponent,
        MachineLearningManagementComponent,
        AppConfigurationComponent,
        MasterTypePopupComponent,
        AppConfigurationComponent,
        JhiConfigurationComponent,
        JhiHealthCheckComponent,
        JhiHealthModalComponent,
        JhiDocsComponent,
        JhiMetricsMonitoringComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    entryComponents: [UserMgmtDeleteDialogComponent, JhiHealthModalComponent, MasterTypePopupComponent, MachineLearningManagementComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ValuationAdminModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
