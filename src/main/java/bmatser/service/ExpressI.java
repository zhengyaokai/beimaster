package bmatser.service;

import bmatser.model.ExpressRequest;
import bmatser.model.ExpressResponse;

import java.util.Map;

/**
 * @ProjectName: gdb_serviceApi
 * @Package: bmatser.util
 * @ClassName: ExpressI
 * @Description: 快递抽象操作接口
 * @Author: zhengyaokai
 * @CreateDate: 2018/8/2 15:47
 */
public interface ExpressI {

    //快递下单
    ExpressResponse expressOrder(ExpressRequest eRequest);

    //快递查单
    ExpressResponse expressQuery(ExpressRequest eRequest);

    //取消订单
    ExpressResponse cancelExpressOrder(ExpressRequest eRequest);

    //结合GDB下物流单的业务处理
    ExpressResponse gdbExpressOrder(ExpressRequest eRequest);

    //打印电子面单或者二维码面单
    String printWaybill(String orderId,Map<String,Object> map);
}
