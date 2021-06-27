import { Component, OnInit } from '@angular/core';
import { HttpEvent, HttpEventType, HttpResponse } from '@angular/common/http';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { pipe } from 'rxjs';
import { filter, map, tap } from 'rxjs/operators';
import { requiredFileType } from 'app/admin/data-ingest/file-upload/upload-file-validators';
import { DataIngestService } from './data-ingest.service';
import { MatTableDataSource } from '@angular/material';

export function uploadProgress<T>(cb: (progress: number) => void) {
    return tap((event: HttpEvent<T>) => {
        if (event.type === HttpEventType.UploadProgress) {
            cb(Math.round((100 * event.loaded) / event.total));
        }
    });
}

export function toResponseBody<T>() {
    return pipe(
        filter((event: HttpEvent<T>) => event.type === HttpEventType.Response),
        map((res: HttpResponse<T>) => res.body)
    );
}

@Component({
    selector: 'jhi-data-ingest',
    templateUrl: './data-ingest.component.html',
    styleUrls: ['data-ingest.component.scss']
})
export class DataIngestComponent implements OnInit {
    displayedColumns = ['errors'];
    displayedColumns2 = ['errors'];
    displayedColumns3 = ['errors'];

    progress = 0;
    progress2 = 0;
    progress3 = 0;

    validation = new FormGroup({
        csvFile: new FormControl(null, [Validators.required, requiredFileType('csv')])
    });

    validation2 = new FormGroup({
        csvFile2: new FormControl(null, [Validators.required, requiredFileType('csv')])
    });

    validation3 = new FormGroup({
        csvFile3: new FormControl(null, [Validators.required, requiredFileType('xlsx')])
    });

    dataSource: MatTableDataSource<string>;
    dataSource2: MatTableDataSource<string>;
    dataSource3: MatTableDataSource<string>;

    success = false;
    success2 = false;
    success3 = false;

    error = false;
    error2 = false;
    error3 = false;

    parsingErrors = false;
    parsingErrors2 = false;
    parsingErrors3 = false;

    lock = false;
    lock2 = false;
    lock3 = false;

    constructor(private dataIngestService: DataIngestService) {}

    ngOnInit(): void {
        this.dataIngestService.getLock().subscribe(response => (this.lock = <any>response), error => (this.lock = true));
        this.dataIngestService.getLock2().subscribe(response => (this.lock2 = <any>response), error => (this.lock2 = true));
        this.dataIngestService.getLock3().subscribe(response => (this.lock3 = <any>response), error => (this.lock3 = true));
    }

    submit() {
        this.success = false;
        if (!this.validation.valid) {
            markAllAsDirty(this.validation);
            return;
        }

        this.dataIngestService
            .uploadCSV(this.validation.value)
            .pipe(
                uploadProgress(progress => (this.progress = progress)),
                toResponseBody()
            )
            .subscribe(
                success => {
                    const response = <any>success;

                    this.progress = 0;
                    this.success = true;
                    this.lock = true;
                    this.validation.reset();

                    if (response.length > 0) {
                        this.parsingErrors = true;
                        this.dataSource = new MatTableDataSource(response);
                    }
                },
                error => {
                    this.progress = 0;
                    this.error = true;
                    this.validation.reset();
                }
            );
    }

    submit2() {
        this.success2 = false;
        if (!this.validation2.valid) {
            markAllAsDirty(this.validation2);
            return;
        }

        this.dataIngestService
            .uploadCSV2(this.validation2.value)
            .pipe(
                uploadProgress(progress => (this.progress2 = progress)),
                toResponseBody()
            )
            .subscribe(
                success => {
                    const response = <any>success;

                    this.progress2 = 0;
                    this.success2 = true;
                    this.lock2 = true;
                    this.validation2.reset();

                    if (response.length > 0) {
                        this.parsingErrors2 = true;
                        this.dataSource2 = new MatTableDataSource(response);
                    }
                },
                error => {
                    this.progress2 = 0;
                    this.error2 = true;
                    this.validation2.reset();
                }
            );
    }

    submit3() {
        this.success3 = false;
        if (!this.validation3.valid) {
            markAllAsDirty(this.validation3);
            return;
        }

        this.dataIngestService
            .uploadCSV3(this.validation3.value)
            .pipe(
                uploadProgress(progress => (this.progress3 = progress)),
                toResponseBody()
            )
            .subscribe(
                success => {
                    const response = <any>success;

                    this.progress3 = 0;
                    this.success3 = true;
                    this.lock3 = true;
                    this.validation3.reset();

                    if (response.length > 0) {
                        this.parsingErrors3 = true;
                        this.dataSource3 = new MatTableDataSource(response);
                    }
                },
                error => {
                    this.progress3 = 0;
                    this.error3 = true;
                    this.validation3.reset();
                }
            );
    }

    hasError(field: string, error: string) {
        const control = this.validation.get(field);
        return control.dirty && control.hasError(error);
    }

    hasError2(field: string, error: string) {
        const control = this.validation2.get(field);
        return control.dirty && control.hasError(error);
    }

    hasError3(field: string, error: string) {
        const control = this.validation3.get(field);
        return control.dirty && control.hasError(error);
    }
}

export function markAllAsDirty(form: FormGroup) {
    for (const control of Object.keys(form.controls)) {
        form.controls[control].markAsDirty();
    }
}
