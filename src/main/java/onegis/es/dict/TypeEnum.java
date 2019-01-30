package onegis.es.dict;

/**
 * 索引库类型枚举
 *
 * @author yz
 * @date 2018/12/10
 */
public enum TypeEnum {

    SOBJCET(1),
    OPEN_TOOLS(2);

    private Integer index;

    TypeEnum(Integer index) {
        this.index = index;
    }


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
