import { Component, Input } from '@angular/core';

@Component({
    selector: 'jhi-data-ingest-progress',
    templateUrl: './data-ingest-progress.component.html'
})
export class DataIngestProgressComponent {
    @Input() progress = 0;
}
