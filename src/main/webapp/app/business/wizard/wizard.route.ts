import { Route } from '@angular/router';
import { WizardComponent } from 'app/business/wizard/wizard.component';
import { UserRouteAccessService } from 'app/core';

export const WIZARD_ROUTE: Route = {
    path: '',
    component: WizardComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'wizard.title'
    },
    canActivate: [UserRouteAccessService]
};
