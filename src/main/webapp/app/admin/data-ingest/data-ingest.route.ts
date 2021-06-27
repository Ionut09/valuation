import { Route } from '@angular/router';

import { DataIngestComponent } from 'app/admin';

export const dataIngestRoute: Route = {
    path: 'data-ingest',
    component: DataIngestComponent,
    data: {
        pageTitle: 'global.data-ingest.page-title'
    }
};
