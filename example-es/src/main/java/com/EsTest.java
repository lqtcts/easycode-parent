//package com;
//
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
//
///**
// */
//public class EsTest {
//    public static void main(String[] args) {
//        new EsTest().TestQuery();
//    }
//
//    public void TestQuery() {
//
//        String index = "ourtmx_pairs_0";
//        String type = "las_pair";
////        int limit =1062471;
//        int limit =100;
//        int page=1;
//        SearchRequestBuilder srb = new EsClient().esClient().prepareSearch(index);
//        BoolQueryBuilder query = boolQuery();
////                .filter(termQuery( "sourceFileInfo.id", "30"));
////        query.must(matchQuery("", "sss"));
//        srb.setQuery(query);SearchHit[] hits1 = hits.getHits();
//        srb.setTypes(type);
//
//        Set<Object> set = new HashSet<>();
//
//        for (int i = 0; i < 99; i++) {
//            srb.setSize(limit);
//            srb.setFrom((page - 1) * limit);
//            srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
////        srb.addHighlightedField(srcTextField, 1280);
//            final SearchResponse searchResp = srb.execute().actionGet();
//            SearchHits hits = searchResp.getHits();
//
//
//            for (SearchHit searchHitFields : hits1) {
//                set.add(searchHitFields.getSource().get("sourceFileInfo"));
//            }
//            page++;
//
//        }
//
//        for (Object o : set) {
//            System.out.println(o);
//        }
//
//
//    }
//}
