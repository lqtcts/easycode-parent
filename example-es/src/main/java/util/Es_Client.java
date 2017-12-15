//package util;
//
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
///**
// * Created by cc on 2016/4/4.
// */
//public class Es_Client {
//    public static final String CLUSTER_NAME = "razor_es"; //实例名称
//    private static final String IP = "192.168.1.1";
//    private static final int PORT = 9300;  //端口
//    //1.设置集群名称：默认是elasticsearch，并设置client.transport.sniff为true，使客户端嗅探整个集群状态，把集群中的其他机器IP加入到客户端
//    //对ES2.0有效
//    private static Settings settings = Settings
//            .settingsBuilder()
//            .put("cluster.name", CLUSTER_NAME)
//            .put("client.transport.sniff", true)
//            .build();
//
//    //创建私有对象
//    private static TransportClient client;
//
//
//    static {
//        try {
//            client = TransportClient.builder().settings(settings).build()
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP), PORT));
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //取得实例
//    public static  TransportClient getTransportClient() {
//        return client;
//    }
//
//}