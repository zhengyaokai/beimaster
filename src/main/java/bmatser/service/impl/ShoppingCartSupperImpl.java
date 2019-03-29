package bmatser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.BuyerConsignAddressMapper;
import bmatser.dao.BuyerInvoiceMapper;
import bmatser.dao.DealerCashMapper;
import bmatser.dao.GoodsPackageMapper;
import bmatser.dao.GrouponActivityMapper;
import bmatser.dao.SellerGoodsMapper;
import bmatser.dao.ShoppingCartMapper;
import bmatser.service.AmountAlgorithmI;
import bmatser.service.FreightAmountI;
import bmatser.service.GbeansServiceI;
import bmatser.service.GoodsActivityI;

@Service
public class ShoppingCartSupperImpl {
	
	@Autowired
	public ShoppingCartMapper cartDao;
	@Autowired
	public AmountAlgorithmI amountAlgorithmI;	
	@Autowired
	public GoodsActivityI activityService;	
	@Autowired
	public SellerGoodsMapper sellerGoodsMapper;
	@Autowired
	public DealerCashMapper dealerCashMapper;
	@Autowired
	public GrouponActivityMapper grouponActivityMapper;
	@Autowired
	public FreightAmountI freightAmountService;
	@Autowired
	public BuyerConsignAddressMapper buyerConsignAddressMapper;
	@Autowired
	public BuyerInvoiceMapper buyerInvoiceI;
	@Autowired
	public GbeansServiceI gbeansServiceI;
	@Autowired
	public GoodsPackageMapper goodsPackageDao;

}
