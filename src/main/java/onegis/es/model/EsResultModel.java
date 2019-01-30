package onegis.es.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Es返回模型
 *
 * @author yz
 * @date 2018/12/14
 */
public class EsResultModel {

    /**
     * 查询总数
     */
    private Long total;

    /**
     * 查询返回数据
     */
    private List<EsResultData> list;

    /**
     * 返回数据(Map)
     */
    private Map<Long, EsResultData> result;

    public EsResultModel(Long total, List<EsResultData> list) {
        this.total = total;
        this.list = list;
    }

    public EsResultModel(Long total, Map<Long, EsResultData> result) {
        this.total = total;
        List<EsResultData> resultDataList = new ArrayList<>();
        for (Map.Entry<Long, EsResultData> entry: result.entrySet()){
            resultDataList.add(entry.getValue());
        }
        this.list = resultDataList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<EsResultData> getList() {
        return list;
    }

    public void setResultDatas(List<EsResultData> list) {
        this.list = list;
    }
}
