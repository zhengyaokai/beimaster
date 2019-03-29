package bmatser.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Trade {
		
	private String fbillno;
	private String fcreatetime;
	private Integer fshopid;
	private String flocknote;
	private Integer fshoptradestatus;
	private Integer fiscod;
	private String fpaytime;
	private String fcustname;
	private String fsalername;
	private BigDecimal fpayamt;
	private BigDecimal fcodamt;
	private BigDecimal fpostamt;
	private BigDecimal fgoodsamt;
	private BigDecimal fdiscountamt;
	private String freceiver;
	private String freceiverstate;
	private String freceivercity;
	private String freceiverdistrict;
	private String freceiveraddress;
	private String freceiverzip;
	private String freceivermobile;
	private String freceiverphone;
	private String fcustmessage;
	private String fsalermessage;
	private List<Item> items;
	private Map<String, String> invoiceaddress;
	private Integer fneedinvoice;
	private Integer finvoicetype;
	private String finvoicetitle;
	private String finvoiceregnumer;
	private String finvoiceregaddress;
	private String finvoiceregtel;
	private String finvoicebanktax;
	private String finvoicebanknumber;
	private String finvoicecontent;
	private Integer finvoiceisgoods = 1;
	private Integer fempnumber;
	private String fempname;
	private String fbankaccountnumber;
	private String fnote;
	private int flocked = 0;
	private String finvreceiver;
	private String finvreceiverstate;
	private String finvreceivercity;
	private String finvreceiverdistrict;
	private String finvreceiveraddress;
	private String finvreceivermobile;
	private String name;
	private String fexpcomnum;
	private int num;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	private String synCode;
	public String getFbillno() {
		return fbillno;
	}
	public void setFbillno(String fbillno) {
		this.fbillno = fbillno;
	}
	public String getFcreatetime() {
		return fcreatetime;
	}
	public void setFcreatetime(String fcreatetime) {
		this.fcreatetime = fcreatetime;
	}
	public Integer getFshopid() {
		return fshopid;
	}
	public void setFshopid(Integer fshopid) {
		this.fshopid = fshopid;
	}
	public String getFlocknote() {
		return flocknote;
	}
	public void setFlocknote(String flocknote) {
		this.flocknote = flocknote;
	}
	public Integer getFshoptradestatus() {
		return fshoptradestatus;
	}
	public void setFshoptradestatus(Integer fshoptradestatus) {
		this.fshoptradestatus = fshoptradestatus;
	}
	public Integer getFiscod() {
		return fiscod;
	}
	public void setFiscod(Integer fiscod) {
		this.fiscod = fiscod;
	}
	public String getFpaytime() {
		return fpaytime;
	}
	public void setFpaytime(String fpaytime) {
		this.fpaytime = fpaytime;
	}
	public String getFcustname() {
		return fcustname;
	}
	public void setFcustname(String fcustname) {
		this.fcustname = fcustname;
	}
	public String getFsalername() {
		return fsalername;
	}
	public void setFsalername(String fsalername) {
		this.fsalername = fsalername;
	}
	public BigDecimal getFpayamt() {
		return fpayamt;
	}
	public void setFpayamt(BigDecimal fpayamt) {
		this.fpayamt = fpayamt;
	}
	public BigDecimal getFcodamt() {
		return fcodamt;
	}
	public void setFcodamt(BigDecimal fcodamt) {
		this.fcodamt = fcodamt;
	}
	public BigDecimal getFpostamt() {
		return fpostamt;
	}
	public void setFpostamt(BigDecimal fpostamt) {
		this.fpostamt = fpostamt;
	}
	public BigDecimal getFgoodsamt() {
		return fgoodsamt;
	}
	public void setFgoodsamt(BigDecimal fgoodsamt) {
		this.fgoodsamt = fgoodsamt;
	}
	public BigDecimal getFdiscountamt() {
		return fdiscountamt;
	}
	public void setFdiscountamt(BigDecimal fdiscountamt) {
		this.fdiscountamt = fdiscountamt;
	}
	public String getFreceiver() {
		return freceiver;
	}
	public void setFreceiver(String freceiver) {
		this.freceiver = freceiver;
	}
	public String getFreceiverstate() {
		return freceiverstate;
	}
	public void setFreceiverstate(String freceiverstate) {
		this.freceiverstate = freceiverstate;
	}
	public String getFreceivercity() {
		return freceivercity;
	}
	public void setFreceivercity(String freceivercity) {
		this.freceivercity = freceivercity;
	}
	public String getFreceiverdistrict() {
		return freceiverdistrict;
	}
	public void setFreceiverdistrict(String freceiverdistrict) {
		this.freceiverdistrict = freceiverdistrict;
	}
	public String getFreceiveraddress() {
		return freceiveraddress;
	}
	public void setFreceiveraddress(String freceiveraddress) {
		this.freceiveraddress = freceiveraddress;
	}
	public String getFreceiverzip() {
		return freceiverzip;
	}
	public void setFreceiverzip(String freceiverzip) {
		this.freceiverzip = freceiverzip;
	}
	public String getFreceivermobile() {
		return freceivermobile;
	}
	public void setFreceivermobile(String freceivermobile) {
		this.freceivermobile = freceivermobile;
	}
	public String getFreceiverphone() {
		return freceiverphone;
	}
	public void setFreceiverphone(String freceiverphone) {
		this.freceiverphone = freceiverphone;
	}
	public String getFcustmessage() {
		return fcustmessage;
	}
	public void setFcustmessage(String fcustmessage) {
		this.fcustmessage = fcustmessage;
	}
	public String getFsalermessage() {
		return fsalermessage;
	}
	public void setFsalermessage(String fsalermessage) {
		this.fsalermessage = fsalermessage;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public Integer getFneedinvoice() {
		return fneedinvoice;
	}
	public void setFneedinvoice(Integer fneedinvoice) {
		this.fneedinvoice = fneedinvoice;
	}
	public Integer getFinvoicetype() {
		return finvoicetype;
	}
	public void setFinvoicetype(Integer finvoicetype) {
		this.finvoicetype = finvoicetype;
	}
	public String getFinvoicetitle() {
		return finvoicetitle;
	}
	public void setFinvoicetitle(String finvoicetitle) {
		this.finvoicetitle = finvoicetitle;
	}
	public String getFinvoiceregnumer() {
		return finvoiceregnumer;
	}
	public void setFinvoiceregnumer(String finvoiceregnumer) {
		this.finvoiceregnumer = finvoiceregnumer;
	}
	public String getFinvoiceregaddress() {
		return finvoiceregaddress;
	}
	public void setFinvoiceregaddress(String finvoiceregaddress) {
		this.finvoiceregaddress = finvoiceregaddress;
	}
	public String getFinvoiceregtel() {
		return finvoiceregtel;
	}
	public void setFinvoiceregtel(String finvoiceregtel) {
		this.finvoiceregtel = finvoiceregtel;
	}
	public String getFinvoicebanktax() {
		return finvoicebanktax;
	}
	public void setFinvoicebanktax(String finvoicebanktax) {
		this.finvoicebanktax = finvoicebanktax;
	}
	public String getFinvoicebanknumber() {
		return finvoicebanknumber;
	}
	public void setFinvoicebanknumber(String finvoicebanknumber) {
		this.finvoicebanknumber = finvoicebanknumber;
	}
	public String getFinvoicecontent() {
		return finvoicecontent;
	}
	public void setFinvoicecontent(String finvoicecontent) {
		this.finvoicecontent = finvoicecontent;
	}
	public Integer getFinvoiceisgoods() {
		return finvoiceisgoods;
	}
	public void setFinvoiceisgoods(Integer finvoiceisgoods) {
		this.finvoiceisgoods = finvoiceisgoods;
	}
	public Integer getFempnumber() {
		return fempnumber;
	}
	public void setFempnumber(Integer fempnumber) {
		this.fempnumber = fempnumber;
	}
	public String getFempname() {
		return fempname;
	}
	public void setFempname(String fempname) {
		this.fempname = fempname;
	}
	public String getFbankaccountnumber() {
		return fbankaccountnumber;
	}
	public void setFbankaccountnumber(String fbankaccountnumber) {
		this.fbankaccountnumber = fbankaccountnumber;
	}
	public String getFnote() {
		return fnote;
	}
	public void setFnote(String fnote) {
		this.fnote = fnote;
	}
	public int getFlocked() {
		return flocked;
	}
	public void setFlocked(int flocked) {
		this.flocked = flocked;
	}
	public String getFinvreceiver() {
		return finvreceiver;
	}
	public void setFinvreceiver(String finvreceiver) {
		this.finvreceiver = finvreceiver;
	}

	public String getFinvreceiveraddress() {
		return finvreceiveraddress;
	}
	public void setFinvreceiveraddress(String finvreceiveraddress) {
		this.finvreceiveraddress = finvreceiveraddress;
	}
	public String getFinvreceivermobile() {
		return finvreceivermobile;
	}
	public void setFinvreceivermobile(String finvreceivermobile) {
		this.finvreceivermobile = finvreceivermobile;
	}
	public String getFinvreceiverstate() {
		return finvreceiverstate;
	}
	public void setFinvreceiverstate(String finvreceiverstate) {
		this.finvreceiverstate = finvreceiverstate;
	}
	public String getFinvreceivercity() {
		return finvreceivercity;
	}
	public void setFinvreceivercity(String finvreceivercity) {
		this.finvreceivercity = finvreceivercity;
	}
	public String getFinvreceiverdistrict() {
		return finvreceiverdistrict;
	}
	public void setFinvreceiverdistrict(String finvreceiverdistrict) {
		this.finvreceiverdistrict = finvreceiverdistrict;
	}
	public Map<String, String> getInvoiceaddress() {
		return invoiceaddress;
	}
	public void setInvoiceaddress(Map<String, String> invoiceaddress) {
		this.invoiceaddress = invoiceaddress;
	}
	public String getSynCode() {
		return synCode;
	}
	public void setSynCode(String synCode) {
		this.synCode = synCode;
	}
	public String getFexpcomnum() {
		return fexpcomnum;
	}
	public void setFexpcomnum(String fexpcomnum) {
		this.fexpcomnum = fexpcomnum;
	}




	
	
}
