package onegis.es.util;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import java.io.IOException;

/**
 * mapping过滤条件设置工具类
 *
 * @author yz
 * @date 2018/12/10
 */
public class Mapping {


    /**
     *  分词器设置拼接工具
     * @return XContentBuilder
     */
    public static XContentBuilder getSObjectMapping(){
        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
            builder.startObject("properties");
            {

                    builder.startObject("name");
                    {
                        builder.field("type", "text");
                        builder.field("analyzer", "ik_max_word");
                    }
                    builder.endObject();
                    builder.startObject("userNikckName");
                    {
                        builder.field("type", "text");
                        builder.field("analyzer", "ik_max_word");
                    }
                    builder.endObject();
                }
                builder.endObject();

        }
        builder.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

}
