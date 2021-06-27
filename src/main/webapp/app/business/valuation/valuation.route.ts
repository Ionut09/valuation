import { Route } from '@angular/router';

import { ValuationInfoComponent } from 'app/business/valuation/valuation.component';
import { UserRouteAccessService } from 'app/core';

export const VALUATION_INFO_ROUTE: Route = {
    path: '',
    component: ValuationInfoComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'valuation.title'
    },
    canActivate: [UserRouteAccessService]
};
