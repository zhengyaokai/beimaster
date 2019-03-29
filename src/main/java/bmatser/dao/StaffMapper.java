package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import bmatser.model.Staff;
import bmatser.pageModel.RegisterPage;
import bmatser.pageModel.StaffPage;

public interface StaffMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Staff record);

    int insertSelective(StaffPage record);

    Staff selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Staff record);

    int updateByPrimaryKey(Staff record);
    
    List<StaffPage> findStaffs(StaffPage staffPage);
    
    StaffPage getStaffById(Integer id);
    
    Long findAllCount(StaffPage staffPage);
    
    StaffPage login(@Param("loginName")String loginName, @Param("password")String password);
    
    Long existLoginName(@Param("loginName")String loginName);
    
    List<Staff> getCustomerManager();
    
    List<Staff> getCustomerService();
    
    List<Staff> getCustomerReferees();
    
    List<Staff> getOwnerManager(Integer id);
    
    List<Staff> getOwnerService(Integer id);
    
    int editStaffPassword(@Param("staffId")Integer staffId, @Param("oldPassword")String oldPassword, @Param("newPassword")String newPassword);
    
    List<Staff> findStaffByPage(@Param("staff")StaffPage staffPage,@Param("showNum")Integer showNum,@Param("size")Integer size);
    
    Long findStaffByPageCount(@Param("staff")StaffPage staffPage);
    
    String findEmail(Integer id);
    /**
     * 根据员工编号获取id
     */
    Integer selectIdByStaffNo(RegisterPage staffNo);
    /**
     * 根据员工邀请码获取id
     */
    Integer selectIdByInvitation(@Param("staffNo")String staffNo);
    
    /**
     * 查询自动分配客服（工控）
     * @author felx
     * @date 2016-5-6
     */
    List<Staff> selectAutoService();
    
    /**
     * 查询自动分配客服（劳保）
     * @author felx
     * @date 2016-9-28
     */
    List<Staff> selectAutoLBService();
    
    int selectServiceAssign(@Param("staffId")Integer staffId);

}