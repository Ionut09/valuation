import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgJhipsterModule } from 'ng-jhipster';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CookieModule } from 'ngx-cookie';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { SharedMaterialModule } from 'app/shared/shared-material.module';

@NgModule({
    imports: [NgbModule, InfiniteScrollModule, CookieModule.forRoot(), FontAwesomeModule, SharedMaterialModule],
    exports: [FormsModule, CommonModule, NgbModule, NgJhipsterModule, InfiniteScrollModule, FontAwesomeModule, SharedMaterialModule]
})
export class ValuationSharedLibsModule {
    static forRoot() {
        return {
            ngModule: ValuationSharedLibsModule
        };
    }
}
