import { Route } from '@angular/router';
import { MachineLearningComponent } from './machine-learning.component';

export const machineLearningRoute: Route = {
    path: 'machine-learning',
    component: MachineLearningComponent,
    data: {
        pageTitle: 'metrics.title'
    }
};
