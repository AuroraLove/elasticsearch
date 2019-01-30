package onegis.es.service.Imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import onegis.es.entity.SearchEntity;
import onegis.es.filter.Esfilter;
import onegis.es.model.EsResultData;
import onegis.es.model.EsResultModel;
import onegis.es.model.ResourceInfo;
import onegis.es.service.EsService;
import onegis.es.util.IdWorker;
import onegis.es.util.JsonUtils;
import onegis.es.util.Mapping;
import onegis.psde.psdm.SObject;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 *  ES查询逻辑处理类
 *
 * @author yz
 * @date 2018/12/06
 */
@Service
public class EsServiceImp implements EsService {

    @Autowired
    private RestHighLevelClient client;

    /**
     *  设置分词器
     *
     * @param index
     * @param type
     * @return boolean
     * @Excpiton Exception
     */
    @Override
    public boolean setMapping(String index, String type) throws Exception{
        CreateIndexRequest indexRequest = new CreateIndexRequest(index, Settings.builder().build());
        client.indices().create(indexRequest,RequestOptions.DEFAULT);
        PutMappingRequest request = new PutMappingRequest(index);
        request.type(type);
        //创建mapping,设置分词器
        request.source(Mapping.getSObjectMapping());
        AcknowledgedResponse acknowledgedResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
        return acknowledgedResponse.isAcknowledged();
    }

    /**
     * 批量存储数据
     *
     * @param objects List<SObject>
     * @return boolean
     * @Excpiton Exception
     */
    @Override
    public  boolean saveSObject(List<SObject> objects) throws Exception {
        return  save(objects,() -> {
            BulkRequest request = new BulkRequest();
            for (SObject object : objects) {
                try {
                    String json = JsonUtils.objectToJson(object);
                    request.add(new IndexRequest("datastore","sobject",String.valueOf(object.getId()))
                            .source(json, XContentType.JSON)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return request;
        });

    }

    /**
     *  批量存储资源对象
     *
     * @param resourceInfo List<ResourceInfo>
     * @return boolean
     * @Excpiton Exception
     */
    @Override
    public boolean saveResource(List<ResourceInfo> resourceInfo) throws Exception{
        return  save(resourceInfo,() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            BulkRequest request = new BulkRequest();
            for (ResourceInfo object : resourceInfo) {
                try {
                    request.add(new IndexRequest("open","resource",String.valueOf(object.getId()))
                            .source(objectMapper.writeValueAsString(object), XContentType.JSON)
                    );
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            return request;
        });
    }

    /**
     *  更新时空对象数据
     *
     * @param sObject
     * @param entity
     * @return boolean
     * @Excpiton Exception
     */
    @Override
    public boolean updataData(SObject sObject,SearchEntity entity) throws Exception{
        UpdateRequest request = new UpdateRequest(entity.getIndex(),entity.getType(),entity.getId())
                .doc(JsonUtils.objectToJson(sObject),XContentType.JSON);
        try {
            UpdateResponse response = client.update(request,RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
            for (ReplicationResponse.ShardInfo.Failure failure :
                    shardInfo.getFailures()) {
                System.out.println( failure.reason());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *  删除索引
     *
     * @param filter
     * @return boolean
     * @Excpiton Exception
     */
    @Override
    public boolean deleteIndex(Esfilter filter) {
        try {
        DeleteIndexRequest request = null;
        if (filter.getIndex() != null){
           request = new DeleteIndexRequest(filter.getIndex());
        }
            AcknowledgedResponse response = client.indices().delete(request,RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *  删除对象数据
     *
     * @param entity
     * @return boolean
     * @Excpiton Exception
     */
    @Override
    public boolean deleteData(SearchEntity entity) throws Exception{

        DeleteRequest request = new DeleteRequest(entity.getIndex())
                                .id(entity.getId()).type(entity.getType());
        DeleteResponse response = client.delete(request,RequestOptions.DEFAULT);
        ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure :
                    shardInfo.getFailures()) {
                System.out.println(failure.reason());
            }
            return false;
        }
        return true;
    }

    /**
     *  全文搜索
     *
     * @param filter
     * @return List<SearchHit>
     * @Excpiton Exception
     */
//    @Override
//    public EsResultModel query(Esfilter filter) {
//        MultiSearchResponse sr = null;
//        Map<Long,EsResultData> result = new HashMap<>();
//        EsResultModel resultModel = null;
//        if (filter.getKeyWord() == null){
//            return null;
//        }
//        MultiSearchRequest request = getMutilSearch(filter);
//        try {
//            sr = client.msearch(request, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        long totalResult = 0L;
//        //hit对象结果集
//        for (MultiSearchResponse.Item item : sr.getResponses()) {
//
//            SearchResponse response = item.getResponse();
//            SearchHits hits = response.getHits();
//            totalResult = hits.getTotalHits();
//
//            for (SearchHit hit: hits){
//                //过滤sobject返回结果 == 拿到版本最新的对象
//                if ("sobject".equals(hit.getType()) && hit.getSourceAsString() != null){
//                    try {
//                        SObject sObject = JsonUtils.jsonToPojo(hit.getSourceAsString(),SObject.class);
//                        if (result.containsKey(sObject.getId())){
//                            EsResultData esResultData = result.get(sObject.getId());
//                            SObject exitSObject = JsonUtils.jsonToPojo(JsonUtils.objectToJson(esResultData.getObject()),SObject.class);
//                            if (sObject.getVersion().getVid() > exitSObject.getVersion().getVid()){
//                                result.remove(sObject.getId());
//                                result.put(sObject.getId(),resultModel(hit));
//                                totalResult--;
//                            }
//                        }else{
//                            result.put(sObject.getId(),resultModel(hit));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }else{
//                    result.put(new IdWorker().nextId(),resultModel(hit));
//                }
//            }
//            resultModel = new EsResultModel(totalResult,result);
//        }
//        return resultModel;
//    }

    @Override
    public EsResultModel query(Esfilter filter) {
        Map<Long,EsResultData> result = new HashMap<>();
        EsResultModel resultModel = null;
        if (filter.getKeyWord() == null){
            return null;
        }
        SearchRequest request = getMutilSearch(filter);
        try {
            SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            long totalResult = 0L;
            //hit对象结果集
            for (SearchHit hit: hits){
                //过滤sobject返回结果 == 拿到版本最新的对象
                if ("sobject".equals(hit.getType()) && hit.getSourceAsString() != null){
                    try {
                        SObject sObject = JsonUtils.jsonToPojo(hit.getSourceAsString(),SObject.class);
                        if (result.containsKey(sObject.getId())){
                            EsResultData esResultData = result.get(sObject.getId());
                            SObject exitSObject = JsonUtils.jsonToPojo(JsonUtils.objectToJson(esResultData.getObject()),SObject.class);
                            if (sObject.getVersion().getVid() > exitSObject.getVersion().getVid()){
                                result.remove(sObject.getId());
                                result.put(sObject.getId(),resultModel(hit));
                                totalResult--;
                            }
                        }else{
                            result.put(sObject.getId(),resultModel(hit));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    result.put(new IdWorker().nextId(),resultModel(hit));
                }
            }
            resultModel = new EsResultModel(totalResult,result);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultModel;

    }

    /**
     * 复杂查询请求策略逻辑
     * @param filter
     * @return
     */
//    private MultiSearchRequest getMutilSearch(Esfilter filter) {
//        MultiSearchRequest request = new MultiSearchRequest();
//        DisMaxQueryBuilder disMaxQueryBuilder=QueryBuilders.disMaxQuery();
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        //复合查询
//        SearchRequest searchRequest = new SearchRequest();
//        //复合查询
//        //精准匹配，权重5
//        QueryBuilder termSearchBuilder = QueryBuilders.termQuery("name.keyword",filter.getKeyWord()).boost(5f);
//        //根据name搜索，权重1
////        QueryBuilder nameSearchRequest = QueryBuilders.matchQuery("name", filter.getKeyWord()).analyzer("ik_max_word").boost(0.8f);
//        //完全含有关键字，权重1
////        QueryBuilder matchAllSearchRequest = QueryBuilders.matchQuery("name", filter.getKeyWord()).analyzer("ik_smart").minimumShouldMatch("80%").boost(1.5f);
//        QueryBuilder queryBuilder = disMaxQueryBuilder.add(termSearchBuilder);
////                .add(nameSearchRequest)
////                .add(matchAllSearchRequest);
//        //设置文本高亮
//        HighlightBuilder highlightBuilder=new HighlightBuilder();
//        highlightBuilder.preTags("<em>");
//        highlightBuilder.postTags("</em>");
//        highlightBuilder.field("name");
//        highlightBuilder.field("userNickName");
//        highlightBuilder.field("attributes.value");
//        highlightBuilder.field("attributes.name");
//        highlightBuilder.field("classify.name");
//        highlightBuilder.field("description");
//
//        searchSourceBuilder.query(QueryBuilders.boolQuery()
//                .should(QueryBuilders.termQuery("name.keyword",filter.getKeyWord()))
////                .should(QueryBuilders.matchQuery("userNickName", filter.getKeyWord()).analyzer("ik_smart").boost(0.5f))
////                .should(QueryBuilders.matchQuery("attributes.value", filter.getKeyWord()).analyzer("ik_smart").boost(0.8f))
////                .should(QueryBuilders.matchQuery("attributes.name", filter.getKeyWord()).analyzer("ik_smart").boost(0.2f))
////                .should(QueryBuilders.matchQuery("description", filter.getKeyWord()).analyzer("ik_smart").boost(0.8f))
//        ).from((filter.getPageNum() - 1) * filter.getPageSize())
//                .size(filter.getPageSize()).highlighter(highlightBuilder);
//        searchRequest.source(searchSourceBuilder);
//        request.add(searchRequest);
////        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("gourpby_id").field("id")
////                .order(BucketOrder.aggregation("top_score",false)).subAggregation(
////                        AggregationBuilders.topHits("top_version").sort("version.vtime", SortOrder.DESC).size(1)
////                                .toXContent()
////                        .scriptField("top_score",new Script(ScriptType.INLINE,))
////                );
////        AggregationBuilder aggregationBuilder = AggregationBuilders.
////        AggregationBuilder aa = AggregationBuilders.terms("top_tgs").field("id")
////                .order(BucketOrder.aggregation("top_hit",false))
////                .subAggregation(
////                );
////        AggregationBuilders.f
//        return request;
//    }

    private SearchRequest  getMutilSearch(Esfilter filter) {
//        MultiSearchRequest request = new MultiSearchRequest();
        DisMaxQueryBuilder disMaxQueryBuilder=QueryBuilders.disMaxQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //复合查询
        SearchRequest searchRequest = new SearchRequest();
        //复合查询
        //精准匹配，权重5
        QueryBuilder termSearchBuilder = QueryBuilders.termQuery("name.keyword",filter.getKeyWord()).boost(5f);
        //根据name搜索，权重1
//        QueryBuilder nameSearchRequest = QueryBuilders.matchQuery("name", filter.getKeyWord()).analyzer("ik_max_word").boost(0.8f);
        //完全含有关键字，权重1
//        QueryBuilder matchAllSearchRequest = QueryBuilders.matchQuery("name", filter.getKeyWord()).analyzer("ik_smart").minimumShouldMatch("80%").boost(1.5f);
        QueryBuilder queryBuilder = disMaxQueryBuilder.add(termSearchBuilder);
//                .add(nameSearchRequest)
//                .add(matchAllSearchRequest);
        //设置文本高亮
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");
        highlightBuilder.field("name");
        highlightBuilder.field("userNickName");
        highlightBuilder.field("attributes.value");
        highlightBuilder.field("attributes.name");
        highlightBuilder.field("classify.name");
        highlightBuilder.field("description");

        searchSourceBuilder.query(QueryBuilders.boolQuery()
                        .should(QueryBuilders.termQuery("name.keyword",filter.getKeyWord()))
//                .should(QueryBuilders.matchQuery("userNickName", filter.getKeyWord()).analyzer("ik_smart").boost(0.5f))
//                .should(QueryBuilders.matchQuery("attributes.value", filter.getKeyWord()).analyzer("ik_smart").boost(0.8f))
//                .should(QueryBuilders.matchQuery("attributes.name", filter.getKeyWord()).analyzer("ik_smart").boost(0.2f))
//                .should(QueryBuilders.matchQuery("description", filter.getKeyWord()).analyzer("ik_smart").boost(0.8f))
        ).from((filter.getPageNum() - 1) * filter.getPageSize())
                .size(filter.getPageSize()).highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);
//        request.add(searchRequest);
//        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("gourpby_id").field("id")
//                .order(BucketOrder.aggregation("top_score",false)).subAggregation(
//                        AggregationBuilders.topHits("top_version").sort("version.vtime", SortOrder.DESC).size(1)
//                                .toXContent()
//                        .scriptField("top_score",new Script(ScriptType.INLINE,))
//                );
//        AggregationBuilder aggregationBuilder = AggregationBuilders.
//        AggregationBuilder aa = AggregationBuilders.terms("top_tgs").field("id")
//                .order(BucketOrder.aggregation("top_hit",false))
//                .subAggregation(
//                );
//        AggregationBuilders.f
        return searchRequest;
    }

    private EsResultData resultModel(SearchHit hit) {
        String[] fields = {"name","userNickName","attributes.value","attributes.name","classify.name","description"};
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        Map<String,Object> result = new HashMap<>();
        for (String field:fields) {
            if (highlightFields.get(field) != null){
                HighlightField name = highlightFields.get(field);
                Text[] texts = name.getFragments();
                StringBuffer sBuffer = new StringBuffer();
                for (Text text : texts) {
                    sBuffer.append(text);
                }
                result.put(field,sBuffer);
            }
        }
        EsResultData model = new EsResultData(hit.getId(),hit.getIndex(),hit.getType(),result,
                hit.getScore(),hit.getSourceAsMap());
        return model;
    }

    protected <T> boolean save(List<T> datas, Supplier<BulkRequest> supplier) throws Exception{
        if (datas.isEmpty()){
            return false;
        }
        long startTime = System.currentTimeMillis();
        BulkRequest request = supplier.get();

        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
        long endTime = System.currentTimeMillis();
        System.out.println("存储完成，共耗时" + (endTime - startTime) + "ms");

        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            if (bulkItemResponse.isFailed()) {
                BulkItemResponse.Failure failure =
                        bulkItemResponse.getFailure();
                System.out.println(failure);
                return false;
            }
        }
        return true;
    }
}
