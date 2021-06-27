import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                {
                    path: 'admin',
                    loadChildren: './admin/admin.module#ValuationAdminModule'
                },
                {
                    path: 'valuation/:id',
                    loadChildren: './business/valuation/valuation.module#ValuationInfoModule'
                },
                {
                    path: 'wizard',
                    loadChildren: './business/wizard/wizard.module#WizardModule'
                },
                {
                    path: 'report/:id',
                    loadChildren: './business/report/report.module#ReportModule'
                },
                ...LAYOUT_ROUTES
            ],
            { useHash: true, enableTracing: DEBUG_INFO_ENABLED }
        )
    ],
    exports: [RouterModule]
})
export class ValuationAppRoutingModule {}
