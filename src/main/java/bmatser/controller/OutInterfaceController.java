package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;

import bmatser.annotations.ResponseDefault;
import bmatser.model.Category;
import bmatser.pageModel.UnitDebit;
import bmatser.service.OutInterfaceI;
/**
 * @author felx
 *	@description 对外接口开发控制层
 */
@RequestMapping("out")
@Controller
public class OutInterfaceController {
	
	@Autowired
	private OutInterfaceI outInterfaceI;
	/**
	 *	@author jxw
	 *	@description 农行单位结算卡推送接口
	 * @param unitDebit 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addUnitDebit",method=RequestMethod.POST)
	@ResponseBody
	@ResponseDefault
	@ApiOperation(value = "单位结算卡推送接口")
	public Object addUnitDebit(@ModelAttribute UnitDebit unitDebit,HttpServletRequest request){
		outInterfaceI.addUnitDebit(unitDebit);
		return null;
	}
}
