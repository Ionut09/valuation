import { Route } from '@angular/router';
import { AppConfigurationComponent } from './app-configuration.component';

export const appConfigurationRoute: Route = {
    path: 'app-configuration',
    component: AppConfigurationComponent,
    data: {
        pageTitle: 'metrics.title'
    }
};
