import { Routes } from '@angular/router';

import {
    auditsRoute,
    configurationRoute,
    docsRoute,
    healthRoute,
    dataIngestRoute,
    metricsRoute,
    userMgmtRoute,
    machineLearningRoute,
    appConfigurationRoute
} from './';

import { UserRouteAccessService } from 'app/core';

const ADMIN_ROUTES = [
    auditsRoute,
    configurationRoute,
    docsRoute,
    healthRoute,
    dataIngestRoute,
    ...userMgmtRoute,
    metricsRoute,
    machineLearningRoute,
    appConfigurationRoute
];

export const adminState: Routes = [
    {
        path: '',
        data: {
            authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        children: ADMIN_ROUTES
    }
];
