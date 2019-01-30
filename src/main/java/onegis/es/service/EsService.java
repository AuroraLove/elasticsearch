package onegis.es.service;

import onegis.es.entity.SearchEntity;
import onegis.es.filter.Esfilter;
import onegis.es.model.EsResultModel;
import onegis.es.model.ResourceInfo;
import onegis.psde.psdm.SObject;

import java.util.List;

/**
 * ES服务接口
 *
 * @author yz
 * @date 2018/12/06
 */
public interface EsService {

    /**
     *  设置分词器
     *
     * @param index
     * @param type
     * @return boolean
     * @Excpiton Exception
     */
    boolean setMapping(String index, String type) throws Exception;

    /**
     *  批量存储时空对象
     *
     * @param sObjects List<T>
     * @return boolean
     * @Excpiton Exception
     */
    boolean saveSObject(List<SObject> sObjects) throws Exception;

    /**
     *  批量存储资源对象
     *
     * @param resourceInfoEntities List<ResourceInfo>
     * @return boolean
     * @Excpiton Exception
     */
    boolean saveResource(List<ResourceInfo> resourceInfoEntities) throws Exception;

    /**
     *  更新时空对象数据
     *
     * @param sObject
     * @param entity
     * @return boolean
     * @Excpiton Exception
     */
    boolean updataData(SObject sObject, SearchEntity entity) throws Exception;

    /**
     *  删除索引
     *
     * @param filter
     * @return boolean
     * @Excpiton Exception
     */
    boolean deleteIndex(Esfilter filter) throws Exception;

    /**
     *  删除对象数据
     *
     * @param entity
     * @return boolean
     * @Excpiton Exception
     */
    boolean deleteData(SearchEntity entity) throws Exception;

    /**
     *  全文搜索
     *
     * @param filter
     * @return List<SearchHit>
     * @Excpiton Exception
     */
    EsResultModel query(Esfilter filter) throws Exception;

}
