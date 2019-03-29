package bmatser.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import bmatser.model.Region;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RegionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Region record);

    int insertSelective(Region record);

    Region selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Region record);

    int updateByPrimaryKey(Region record);
    
    Map selectRegion(Map mp);
    
    String selectRegionString(String id);
    
    String selectJDRegionString(String id);

    List<Region> findNamesInIds(@Param("set") Set<Integer> ids);
}