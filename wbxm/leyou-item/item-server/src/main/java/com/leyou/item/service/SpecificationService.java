package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-04-04 13:51
 */
public interface SpecificationService {
    List<SpecGroup> queryGroupsByCid(Long cid);

    List<SpecParam> queryParams(Long gid,Long cid,Boolean generic,Boolean searching);

    void saveParams(SpecParam specParam);

    void updateParams(SpecParam specParam);

    void deleteParams(Long id);

    void saveGroup(SpecGroup specGroup);

    void deleteGroup(Long id);

    void updateGroup(SpecGroup specGroup);

    List<SpecGroup> queryGroupsWithParam(Long cid);
}
