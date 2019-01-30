package onegis.es.entity;

/**
 * 搜索模型
 *
 * @author yz
 * @date 2018/12/10
 */
public class SearchEntity {

    private String id;

    /**
     * 索引，对应数据库表空间
     */
    private String index;

    /**
     * 类型，对应数据库表
     */
    private String type;

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
}