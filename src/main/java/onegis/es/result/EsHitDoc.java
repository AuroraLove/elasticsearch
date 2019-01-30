package onegis.es.result;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>对象搜索结果</p>
 *
 * @Author Appleyk
 * @Blob https://blog.csdn.net/appleyk
 * @Date Created on 下午 1:05 2018-12-27
 * @Version V.1.0.1
 */
public class EsHitDoc implements Serializable {


    /**
     * 索引
     */
    private String index;

    /**
     * 索引数据【文档】类型
     */
    private String type;

    /**
     * 文档ID
     */
    private Long id;

    /**
     * 分数
     */
    private Float score;

    /**
     * 文档对象
     */
    private Map<String,Object> object;

    /**
     *  ES高亮文本内容
     */
    private Map<String,Object> highlight = new HashMap<>();


    public EsHitDoc(){
    }

    public EsHitDoc(SearchHit hit,String[] fields){
        this.id = Long.valueOf(hit.getId());
        this.index = hit.getIndex();
        this.type = hit.getType();
        this.score = hit.getScore();
        setHightLight(fields,hit.getHighlightFields());
        this.object = hit.getSourceAsMap();
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Object> getHighlight() {
        return highlight;
    }

    public void setHighlight(Map<String, Object> highlight) {
        this.highlight = highlight;
    }

    public Map<String, Object> getObject() {
        return object;
    }

    public void setObject(Map<String, Object> object) {
        this.object = object;
    }

    /**
     * 设置高亮字段
     */
    public void setHightLight(String[] fields,Map<String, HighlightField> highlightFields){

        for (String field:fields) {
            if (highlightFields.get(field) != null) {
                HighlightField name = highlightFields.get(field);
                Text[] texts = name.getFragments();
                List<String> hlText = new ArrayList<>();
                for (Text text : texts) {
                    hlText.add(text.string());
                }
                this.highlight.put(field, hlText);
            }
        }
    }
}
