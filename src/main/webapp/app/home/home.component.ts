import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, AccountService, LoginModalService } from 'app/core';
import { Router } from '@angular/router';
import { HomeService } from 'app/home/home.service';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    displayedColumns: string[] = [
        'id',
        'manufacturer',
        'model',
        'version',
        'trim',
        'fabricationDate',
        'registrationDate',
        'valuationDate',
        'numberOfKilometers',
        'vehicleId'
    ];
    dataSource: MatTableDataSource<any>;

    dataLoaded = false;

    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;

    constructor(
        private accountService: AccountService,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private router: Router,
        private homeService: HomeService
    ) {}

    ngOnInit() {
        this.accountService.identity().then((account: Account) => {
            this.account = account;

            this.homeService.getValuationHistory().subscribe(response => {
                this.dataLoaded = true;
                this.dataSource = new MatTableDataSource(<any>response);

                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
                this.dataSource.filterPredicate = function(data, filter: string): boolean {
                    return (
                        data.vehicleDTO.manufacturer.toLowerCase().includes(filter) ||
                        data.vehicleDTO.model.toLowerCase().includes(filter) ||
                        data.vehicleDTO.trimLevel.toLowerCase().includes(filter) ||
                        data.vehicleDTO.version.toLowerCase().includes(filter)
                    );
                };
            });
        });

        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.accountService.identity().then(account => {
                this.account = account;

                this.homeService.getValuationHistory().subscribe(response => {
                    this.dataLoaded = true;
                    this.dataSource = new MatTableDataSource(<any>response);

                    this.dataSource.paginator = this.paginator;
                    this.dataSource.sort = this.sort;
                    this.dataSource.filterPredicate = function(data, filter: string): boolean {
                        return (
                            data.vehicleDTO.manufacturer.toLowerCase().includes(filter) ||
                            data.vehicleDTO.model.toLowerCase().includes(filter) ||
                            data.vehicleDTO.trimLevel.toLowerCase().includes(filter) ||
                            data.vehicleDTO.version.toLowerCase().includes(filter)
                        );
                    };
                });
            });
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    navigateToWizard() {
        this.router.navigateByUrl('/wizard');
    }

    handelValuationSelection(row) {
        this.router.navigate(['/valuation', row.valuation.id]);
    }

    applyFilter(filterValue: string) {
        this.dataSource.filter = filterValue.trim().toLowerCase();
    }
}
