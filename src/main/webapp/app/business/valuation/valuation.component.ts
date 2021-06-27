import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, UrlSerializer } from '@angular/router';
import { ValuationService } from 'app/business/valuation/valuation.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { ChartEvent, ChartType } from 'ng-chartist';
import { IBarChartOptions, IChartistAnimationOptions } from 'chartist';

import * as moment from 'moment';
import { JhiLanguageService } from 'ng-jhipster';

import * as Chartist from 'chartist';
import * as ChartistTooltip from 'chartist-plugin-tooltips-updated';

@Component({
    selector: 'jhi-valuation-info',
    templateUrl: './valuation.component.html',
    styleUrls: ['valuation.component.scss']
})
export class ValuationInfoComponent implements OnInit {
    language = 'en';

    tradeInPrice;
    offerPrice;
    retailPrice;
    offerPricePredicted;

    type: ChartType = 'Line';
    data = {
        labels: [],
        series: [[], []]
    };

    forecastData = {
        labels: [],
        series: [[], []]
    };

    options: IBarChartOptions = {
        axisX: {
            showGrid: false,
            labelInterpolationFnc: value => {
                moment.locale(this.language);
                return moment(value).format('MMM YYYY');
            }
        },
        height: 340,
        plugins: [ChartistTooltip()],
        low: 250,
        high: 50000
    };

    events: ChartEvent = {
        draw: data => {
            if (data.type === 'line') {
                data.element.animate({
                    y2: <IChartistAnimationOptions>{
                        dur: '0.5s',
                        from: data.y1,
                        to: data.y2,
                        easing: 'easeOutQuad'
                    }
                });
            }
        }
    };

    showChart = true;

    valuation: any;
    selectedExtraOptions: any;
    dataLoaded = false;

    similarAds: any;
    similarAdsLoaded = false;

    valuationResult = 0;

    @BlockUI() blockUI: NgBlockUI;

    constructor(
        private route: ActivatedRoute,
        private valuationService: ValuationService,
        private router: Router,
        private serializer: UrlSerializer,
        private languageService: JhiLanguageService
    ) {}

    ngOnInit(): void {
        this.languageService.getCurrent().then(lang => {
            this.language = lang;
        });

        this.route.paramMap.subscribe(params => {
            const valuationId = +params.get('id');
            if (valuationId) {
                this.valuationService.getValuation(valuationId).subscribe(response => {
                    this.dataLoaded = true;
                    this.valuation = response;

                    console.log(this.valuation);
                    this.valuationResult = this.valuation.valuation.valuationPrice;
                    this.computeSellingPrices();

                    this.valuationService
                        .getExtraOptions(this.valuation.valuation.id)
                        .subscribe(resp => (this.selectedExtraOptions = resp));

                    this.valuationService.getMasterTypePoints(this.valuation.valuation.id).subscribe(_resp => {
                        const _data = <any>_resp;

                        if (_data.length === 0) {
                            this.showChart = false;
                            return;
                        }

                        _data.forEach(el => {
                            this.data.labels.push(new Date(el.date));
                            this.data.series[0].push(Number(el.value * 1.19).toFixed(2));
                        });

                        this.data.series[1][this.data.series[0].length - 1] = Number(this.offerPrice).toFixed(2);

                        let highest = 0;
                        _data.forEach(el => {
                            if (highest < el.value * 1.19) {
                                highest = el.value * 1.19;
                            }
                        });

                        this.options.low = 0;
                        this.options.high = highest + 10000;

                        this.data.labels = this.data.labels.reverse();
                        this.data.series[0] = this.data.series[0].reverse();

                        this.options = { ...this.options };
                        this.data = { ...this.data };
                    });
                });
            }
        });
    }
    getImageUrl() {
        return 'photos/' + this.valuation.imageUrl + '.JPG';
    }

    getAdUrl(ad) {
        return 'https://www.autovit.ro/anunt/' + ad.adURI;
    }

    handleSimilarAdsClick() {
        this.valuationService.getAutovitParameters(this.valuation.valuation.id).subscribe(response => {
            const mainlUrl = 'https://www.autovit.ro';

            const _response = <any>response;

            const linkParams = {
                make: _response.manufacturer,
                model: _response.model,
                year: 'de-la-' + _response.modelYear
            };

            const queryParams = {
                'search[filter_float_year:to]': _response.modelYear,
                'search[filter_float_mileage:from]': +_response.numberOfKilometers - 30000,
                'search[filter_float_mileage:to]': +_response.numberOfKilometers + 30000,
                'search[filter_enum_gearbox][0]': _response.transmission.toLowerCase()
            };
            const tree = this.router.createUrlTree(['autoturisme', linkParams.make, linkParams.model, linkParams.year, ''], {
                queryParams
            });
            console.log(mainlUrl + tree.toString());
            window.open(mainlUrl + tree.toString(), '_blank');
        });
    }

    computeSellingPrices() {
        let tireAttrition = 0;
        let bodyDamages = 0;

        if (this.valuation.valuation.attritionValue < 0) {
            tireAttrition = this.valuation.valuation.attritionValue;
        }

        if (this.valuation.valuation.bodyDamageValue < 0) {
            bodyDamages = this.valuation.valuation.bodyDamageValue;
        }

        if (this.valuation.valuation.priceAsNew >= 45000) {
            this.offerPricePredicted = this.valuation.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.1 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 1.5) / 100) * 1.06 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 1.5) / 100) * 1.1 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
        } else if (this.valuation.valuation.priceAsNew >= 30000) {
            this.offerPricePredicted = this.valuation.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.1 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 2) / 100) * 1.06 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 2) / 100) * 1.1 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
        } else if (this.valuation.valuation.priceAsNew >= 20000) {
            this.offerPricePredicted = this.valuation.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.12 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 2.5) / 100) * 1.07 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 2.5) / 100) * 1.12 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
        } else if (this.valuation.valuation.priceAsNew >= 15000) {
            this.offerPricePredicted = this.valuation.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.12 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 3) / 100) * 1.07 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 3) / 100) * 1.12 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
        } else if (this.valuation.valuation.priceAsNew >= 10000) {
            this.offerPricePredicted = this.valuation.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.12 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 3.5) / 100) * 1.08 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 3.5) / 100) * 1.12 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
        } else {
            this.offerPricePredicted = this.valuation.valuation.valuationPrice;
            this.tradeInPrice = (this.offerPricePredicted / 1.14 - Math.abs(tireAttrition) - Math.abs(bodyDamages)) * 1.19;
            this.retailPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 4) / 100) * 1.09 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.offerPrice =
                (this.tradeInPrice + (this.valuation.valuation.priceAsNew * 4) / 100) * 1.14 +
                this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
            this.tradeInPrice += this.valuation.valuation.extraOptionsDepreciationPrice * 1.19;
        }

        this.valuationService.getForecastTypePoints(this.valuation.valuation.id, this.offerPrice).subscribe(_resp2 => {
            const _data2 = <any>_resp2;

            _data2.forEach(el => {
                this.forecastData.labels.push(new Date(el.date));
                this.forecastData.series[0].push(Number(el.value).toFixed(2));
            });

            this.forecastData = { ...this.forecastData };
        });
    }

    downloadPDF() {
        this.router.navigate(['/report', this.valuation.valuation.id]);
    }

    navigateToWizard() {
        this.router.navigateByUrl('/wizard');
    }
}
