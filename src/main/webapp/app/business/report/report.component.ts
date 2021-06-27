import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReportService } from 'app/business/report/report.service';
import { JhiLanguageService } from 'ng-jhipster';

@Component({
    selector: 'jhi-report',
    templateUrl: './report.component.html',
    styleUrls: ['report.component.scss']
})
export class ReportComponent implements OnInit {
    data;
    dataLoaded = false;

    stdOptions = [];
    extraOptions = [];

    tradeInPrice;
    offerPrice;
    retailPrice;
    offerPricePredicted;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private languageService: JhiLanguageService,
        private reportService: ReportService
    ) {}

    handleButton() {
        window.print();
    }

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            const valuationId = +params.get('id');

            if (valuationId !== 0) {
                this.reportService.getData(valuationId).subscribe(response => {
                    this.data = response;
                    this.dataLoaded = true;

                    this.computeSellingPrices();

                    this.reportService.getStdOptions(this.data.valuation.vehicleId).subscribe(resp => {
                        this.languageService.getCurrent().then(lang => {
                            if (lang === 'ro') {
                                this.stdOptions = (<any>resp).filter(element => element.languageId === 55);
                            } else {
                                this.stdOptions = (<any>resp).filter(element => element.languageId === 19);
                            }
                        });
                    });

                    this.reportService.getExtraOptions(this.data.valuation.id).subscribe(resp => (this.extraOptions = <any>resp));
                });
            }
        });
    }

    getImageUrl(data) {
        return 'photos/' + data.imageUrl + '.JPG';
    }

    computeSellingPrices() {
        let tireAttrition = 0;
        let bodyDamages = 0;

        if (this.data.valuation.attritionValue < 0) {
            tireAttrition = this.data.valuation.attritionValue;
        }

        if (this.data.valuation.bodyDamageValue < 0) {
            bodyDamages = this.data.valuation.bodyDamageValue;
        }

        if (this.data.valuation.priceAsNew >= 45000) {
            this.offerPricePredicted = this.data.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.1 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 1.5) / 100) * 1.06 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 1.5) / 100) * 1.1 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.data.valuation.extraOptionsDepreciationPrice * 1.19;
        } else if (this.data.valuation.priceAsNew >= 30000) {
            this.offerPricePredicted = this.data.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.1 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 2) / 100) * 1.06 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 2) / 100) * 1.1 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.data.valuation.extraOptionsDepreciationPrice * 1.19;
        } else if (this.data.valuation.priceAsNew >= 20000) {
            this.offerPricePredicted = this.data.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.12 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 2.5) / 100) * 1.07 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 2.5) / 100) * 1.12 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.data.valuation.extraOptionsDepreciationPrice * 1.19;
        } else if (this.data.valuation.priceAsNew >= 15000) {
            this.offerPricePredicted = this.data.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.12 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 3) / 100) * 1.07 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 3) / 100) * 1.12 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.data.valuation.extraOptionsDepreciationPrice * 1.19;
        } else if (this.data.valuation.priceAsNew >= 10000) {
            this.offerPricePredicted = this.data.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.12 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 3.5) / 100) * 1.08 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 3.5) / 100) * 1.12 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.data.valuation.extraOptionsDepreciationPrice * 1.19;
        } else {
            this.offerPricePredicted = this.data.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.14 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 4) / 100) * 1.09 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.data.valuation.priceAsNew * 4) / 100) * 1.14 +
                this.data.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.data.valuation.extraOptionsDepreciationPrice * 1.19;
        }
    }
}
