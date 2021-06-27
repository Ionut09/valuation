import { Route } from '@angular/router';

import { ReportComponent } from 'app/business/report/report.component';
import { UserRouteAccessService } from 'app/core';

export const REPORT_ROUTE: Route = {
    path: '',
    component: ReportComponent,
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService]
};
