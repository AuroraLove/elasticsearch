package onegis.es;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import onegis.es.service.EsService;
import onegis.es.service.GetData;
import onegis.psde.psdm.SObject;
import onegis.psde.util.JsonUtils;
import onegis.result.response.ResponseResult;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import onegis.es.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsApplication.class)
public class EsApplicationTests {

    @Autowired
    private EsService esService;

    @Autowired
    private GetData template;

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void contextLoads() {
        System.out.println(Mapping.getSObjectMapping());
    }

    @Test
    public void importTest()throws Exception{
//        Timedtask.reportCurrentTime();
    }


    @Test
    public  void getResourceFile() throws  Exception{

        File file1 = ResourceUtils.getFile("classpath:time.ini");
        System.out.println(file1);
        URL url = ResourceUtils.getURL("time.ini");
        System.out.println(url);
        File file = new File(url.toURI());
        System.out.println(file);
    }

    @Test
    public void query() throws Exception{

        String sTime = "2018-12-07 09:00:00";
        String eTime = "2018-12-07 10:00:00";

        /**
         * 总页数
         */
        int total = 0;
        /**
         * 显示第几页
         */
        int pageNum = 1;
        /**
         * 一页显示多少
         */
        int pageSize= 500;
        /**
         * 总页数
         */
        int pages = 0;
        /**
         * 最后一页的大小
         */
        int lastCount =0;

        System.out.println("数据拉取开始时间:"+sTime);
        System.out.println("数据拉取结束时间:"+eTime);
        ResponseResult result = template.getSObject1(sTime, eTime,pageNum,pageSize);
        LinkedHashMap map = (LinkedHashMap) result.getData();

        if(map.get("total")!=null){
            // 总记录数
            total = (Integer) map.get("total");
            pages = total/pageSize;
            lastCount = total%pageSize;
        }

        for(int i = 0;i<pages;i++){
            String str= queryByPages(sTime,eTime ,pageNum ,pageSize );
            str+=" -- "+(i+1);
            pageNum++;
            System.out.println(str);
        }

        String str= queryByPages(sTime,eTime ,pageNum ,lastCount );
        System.out.println(str);

    }

    public String queryByPages(String sTime,String eTime,int pageNum,int pageSize) throws  Exception{
        String resStr;
        ResponseResult result = template.getSObject1(sTime, eTime,pageNum,pageSize);
        LinkedHashMap map = (LinkedHashMap) result.getData();
        if( map.get("list") != null){
            List<SObject> sObjects = new ObjectMapper().convertValue(map.get("list"), new TypeReference<List<SObject>>() { });
            esService.saveSObject(sObjects);
            resStr = "存储：" + sObjects.size() + " 条记录";
        }else {
            resStr = "此时间间隔中无数据";
        }
        return  resStr;
    }

    @Test
    public  void termQuery() throws  Exception{
        SearchRequest searchRequest = new SearchRequest();
        QueryBuilder termSearchBuilder = QueryBuilders.termQuery("name.keyword","中科院肿瘤医院").boost(5f);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                        .should(termSearchBuilder));
//                .should(QueryBuilders.matchQuery("userNickName", filter.getKeyWord()).analyzer("ik_smart").boost(0.5f))
//                .should(QueryBuilders.matchQuery("attributes.value", filter.getKeyWord()).analyzer("ik_smart").boost(0.8f))
//                .should(QueryBuilders.matchQuery("attributes.name", filter.getKeyWord()).analyzer("ik_smart").boost(0.2f))
        searchRequest.source(searchSourceBuilder);
//        client.explain();
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits);
    }
}
