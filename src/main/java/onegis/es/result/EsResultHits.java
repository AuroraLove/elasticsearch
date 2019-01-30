package onegis.es.result;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @Author Appleyk
 * @Blob https://blog.csdn.net/appleyk
 * @Date Created on 下午 1:15 2018-12-27
 * @Version V.1.0.1
 */
public class EsResultHits {

    /**
     * 总命中数
     */
    private Long total;

    /**
     * 最大分数
     */
    private Float maxScore ;

    /**
     * 结果集
     */
    private List<EsHitDoc> list = new ArrayList<>();


    public EsResultHits(SearchHits hits,String[] fields){

        this.total = hits.getTotalHits();
        this.maxScore = hits.getMaxScore();
        SearchHit[] hitArray = hits.getHits();
        for (SearchHit hit : hitArray) {
            EsHitDoc esHitDoc = new EsHitDoc(hit,fields);
            this.list.add(esHitDoc);
        }
    }

    public Float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Float maxScore) {
        this.maxScore = maxScore;
    }

    public List<EsHitDoc> getList() {
        return list;
    }

    public void setList(List<EsHitDoc> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
