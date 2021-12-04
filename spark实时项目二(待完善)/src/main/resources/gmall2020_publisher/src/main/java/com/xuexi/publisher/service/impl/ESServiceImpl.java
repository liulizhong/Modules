package 项目二实时项目.gmall2020_publisher.src.main.java.com.xuexi.publisher.service.impl;

import com.xuexi.publisher.service.ESService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: lizhong.liu
 * Date: 2020/9/15
 * Desc:  ES相关操作的具体实现
 */
@Service
public class ESServiceImpl implements ESService {

    @Autowired
    JestClient jestClient;

    //获取日活总数
    @Override
    public Long getDauTotal(String date) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(new MatchAllQueryBuilder());
        String query = sourceBuilder.toString();
        //拼接查询的索引名称
        String indexName = "gmall2020_dau_info_" + date + "-query";
        Search search = new Search.Builder(query)
                .addIndex(indexName)
                .addType("_doc")
                .build();
        Long total = 0L;
        try {
            //获取当前日期对应索引的所有记录
            SearchResult searchResult = jestClient.execute(search);
            if (searchResult.getTotal() != null) {
                total = searchResult.getTotal();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ES查询异常");
        }
        return total;
    }

    /*
    GET /gmall0421_dau_info_2020-09-15-query/_search
    {
         "aggs": {
         "groupby_hr": {
             "terms": {
                 "field": "hr",
                         "size": 24
             }
         }
     }
     }*/
    @Override
    public Map<String, Long> getDauHour(String date) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("groupby_hr").field("hr").size(24);
        sourceBuilder.aggregation(termsAggregationBuilder);
        String query = sourceBuilder.toString();
        //拼接查询的索引名称
        String indexName = "gmall0421_dau_info_" + date + "-query";
        Search search = new Search.Builder(query)
                .addIndex(indexName)
                .addType("_doc")
                .build();

        try {
            Map<String, Long> dauMap = new HashMap<String, Long>();
            SearchResult searchResult = jestClient.execute(search);
            if (searchResult.getAggregations().getTermsAggregation("groupby_hr") != null) {
                List<TermsAggregation.Entry> buckets = searchResult.getAggregations().getTermsAggregation("groupby_hr").getBuckets();
                for (TermsAggregation.Entry bucket : buckets) {
                    dauMap.put(bucket.getKey(), bucket.getCount());
                }
            }
            return dauMap;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ES查询异常");
        }
    }
}
