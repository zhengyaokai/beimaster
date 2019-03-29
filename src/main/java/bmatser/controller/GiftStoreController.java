package bmatser.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.model.DealerScore;
import bmatser.model.Gift;
import bmatser.service.GiftStoreI;
import bmatser.service.PersonalDataI;
import bmatser.util.JSONUtil;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("/giftStore")
@Api(value="giftStore", description="积分商城")
public class GiftStoreController
{
	@Autowired
	private GiftStoreI giftStoreI;
	@Autowired
	private PersonalDataI personalDataI;
	/**
	 * 积分商城
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ApiOperation(value = "积分商城" , response = Gift.class)
	public String getlist(@ApiParam(value="jsonp返回")@RequestParam String callback,HttpServletRequest request,
			@ApiParam(value="全部商品 1:0-9999积分;2:10000-49999积分;3:50000以上")@RequestParam(required=false) String type){
		ResponseMsg msg = new ResponseMsg();
		String isRecommend = request.getParameter("isRecommend");
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		try {
			Map map = giftStoreI.findGiftList(isRecommend,type);
			if(null != loginInfo && null != loginInfo.getDealerId()){
				Map scoreMap = personalDataI.findRegister(loginInfo.getDealerId());
				map.put("score", scoreMap.get("score"));
			}
			msg.setData(map);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return callback + "(" + JSONUtil.objectToJson(msg).toString() + ")";
	}
	/**
	 * 积分商品
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/goods",method=RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ApiOperation(value = "积分商品" )
	public String getlist(@ApiParam(value="积分商品ID") @RequestParam(required=false) String id,
			@ApiParam(value="jsonp返回")@RequestParam String callback,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		/*String id = request.getParameter("id");*/
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		try {
			Map map = giftStoreI.findGiftById(id);
			if(null != loginInfo && null != loginInfo.getDealerId()){
				Map scoreMap = personalDataI.findRegister(loginInfo.getDealerId());
				map.put("score", scoreMap.get("score"));
			}
			msg.setData(map);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return callback + "(" + JSONUtil.objectToJson(msg).toString() + ")";
	}

		/**
		 * app积分商城
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value="/app/{page}",method=RequestMethod.GET)
		@ApiOperation(value = "app积分商城" , response = Gift.class)
		public ResponseMsg getlist(@ApiParam(value="页数") @PathVariable int page,
				@ApiParam(value="行数") @RequestParam(required=false) String rows,@ApiParam(value="dealerId")@RequestParam(required=false) String dealerId,
				@ApiParam(value="是否推荐（1：是，0：否）")@RequestParam(required=false) String isRecommend,
				@ApiParam(value="全部商品 1:0-9999积分;2:10000-49999积分;3:50000以上")@RequestParam(required=false) String type){
			/*String isRecommend = request.getParameter("isRecommend");*/
			ResponseMsg msg = new ResponseMsg();
			int row = 10;
			if(StringUtils.isNotBlank(rows)){
				row = Integer.parseInt(rows);
			}
			try {
				Map map = giftStoreI.findAppGiftList(isRecommend,type,(page-1)*row,row);
				if(StringUtils.isNotBlank(dealerId)){
					Map scoreMap = personalDataI.findRegister(dealerId);
					map.put("score", scoreMap.get("score"));
				}
				msg.setData(map);
				msg.setCode(0);
			} catch (Exception e) {
				msg.setError(e);
			}
			return msg;
		}
		/**
		 * APP积分商品
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value="/app/goods",method=RequestMethod.GET)
		@ApiOperation(value = "积分商品" )
		public ResponseMsg getApplist(@ApiParam(value="积分商品ID") @RequestParam String id,
				@ApiParam(value="dealerId")@RequestParam(required=false) String dealerId){
			ResponseMsg msg = new ResponseMsg();
			/*String id = request.getParameter("id");*/
			/*LoginInfo loginInfo = MemberTools.isSaasLogin(request);*/
			try {
				Map map = giftStoreI.findGiftById(id);
				if(StringUtils.isNotBlank(dealerId)){
					Map scoreMap = personalDataI.findRegister(dealerId);
					map.put("score", scoreMap.get("score"));
				}
				msg.setData(map);
				msg.setCode(0);
			} catch (Exception e) {
				msg.setError(e);
			}
			return msg;
		}
		
	

	
}
