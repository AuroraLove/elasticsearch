package onegis.es.service;

import onegis.result.response.ResponseResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程数据拉取接口
 *
 * @author yz
 * @date 2018/12/16
 */
@FeignClient(url = "http://bt1.geosts.ac.cn/api/dae/datastore" ,name = "es-client")
public interface GetData {

    @RequestMapping(value="/rest/v0.1.0/datastore/object/changeset/query",method= RequestMethod.GET)
    ResponseResult getSObject(@RequestParam("beginTime") String sTime, @RequestParam("endTime") String eTime);

    @RequestMapping(value="/rest/v0.1.0/datastore/object/changeset/query",method= RequestMethod.GET)
    ResponseResult getSObject1(@RequestParam("beginTime") String sTime, @RequestParam("endTime") String eTime,
                               @RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize);

}
