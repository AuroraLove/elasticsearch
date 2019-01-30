package onegis.es.controller;

import onegis.es.entity.SearchEntity;
import onegis.es.filter.Esfilter;
import onegis.es.model.EsResultModel;
import onegis.es.model.ResourceInfo;
import onegis.es.service.EsService;
import onegis.psde.psdm.SObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  ES查询REST接口
 *
 * @author yz
 * @date 2018/12/06
 */
@RestController
@RequestMapping("/api/v1/es")
public class EsController {

    @Autowired
    private EsService esService;

    @PostMapping("/setMapping")
    public boolean setMapping(String index, String type) throws Exception{
        return esService.setMapping(index,type);
    }

    @PostMapping("/saveSObject")
    public boolean saveSObject(@RequestBody List<SObject> sObjects)throws Exception{
        return esService.saveSObject(sObjects);
    }

    @PostMapping("/saveResource")
    public boolean saveResource(@RequestBody List<ResourceInfo> resourceInfoEntities)throws Exception{
        return esService.saveResource(resourceInfoEntities);
    }

    @PutMapping("/updataData")
    public boolean updataData(@RequestBody SObject sObject, SearchEntity entity) throws Exception{
        return esService.updataData(sObject,entity);
    }

    @DeleteMapping("/deleteIndex")
    public boolean deleteIndex(Esfilter filter) throws Exception{

        return esService.deleteIndex(filter);
    }


    @DeleteMapping("/deleteData")
    public boolean deleteData(SearchEntity entity) throws Exception{
        return esService.deleteData(entity);
    }

    @GetMapping("/search")
    public EsResultModel search(String keyWord, @RequestParam(required = false,defaultValue = "10") Integer pageSize
            , @RequestParam(required = false,defaultValue = "1")Integer pageNum) throws Exception{
        Esfilter esfilter = new Esfilter(keyWord,pageNum,pageSize);
        return esService.query(esfilter);
    }

}
