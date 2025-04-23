import { TestBed } from '@angular/core/testing';

import { PurchaseListService } from './purchase-list.service';

describe('PurchaseListService', () => {
  let service: PurchaseListService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PurchaseListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
