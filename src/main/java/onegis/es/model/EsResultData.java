package onegis.es.model;

import java.util.Map;

/**
 * Es返回值包装
 *
 * @author yz
 * @date 2018/12/14
 */
public class EsResultData {

    /**
     *  ES索引Id
     */
    private String id;

    /**
     *  ES索引名称
     */
    private String index;

    /**
     *  ES索引类型
     */
    private String type;

    /**
     *  ES返回结果评分
     */
    private Float score;

    /**
     *  ES返回值内容（SourceAsMap）
     */
    private  Map<String, Object> object;

    /**
     *  ES高亮文本内容
     */
    private Map<String,Object> highlight;

    public EsResultData(String id, String index, String type, Map<String,Object> highlight, float score, Map<String, Object> object) {
        this.id = id;
        this.index = index;
        this.type = type;
        this.score = score;
        this.object = object;
        this.highlight = highlight;
    }

    public EsResultData() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Map<String, Object> getObject() {
        return object;
    }

    public void setObject(Map<String, Object> object) {
        this.object = object;
    }

    public Map<String, Object> getHighlight() {
        return highlight;
    }

    public void setHighlight(Map<String, Object> highlight) {
        this.highlight = highlight;
    }
}
