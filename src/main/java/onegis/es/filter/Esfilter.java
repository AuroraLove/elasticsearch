package onegis.es.filter;

/**
 * 搜索过滤类
 *
 * @author yz
 * @date 2018/12/10
 */
public class Esfilter {

    /**
     * 搜索关键字
     */
    private String keyWord;

    /**
     * 索引
     */
    private String index;

    private Integer pageNum;

    private Integer pageSize;

    public Esfilter() {

    }

    public Esfilter(String keyWord, Integer pageNum, Integer pageSize) {
        this.keyWord = keyWord;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
