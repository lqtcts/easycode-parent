//package com;
//
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
///**
// * Created by 0 on 2017/4/6.
// */
//public class EsClient {
//
//    public TransportClient transportClient;
//    private String esHosts = "192.168.16.62";
//    private int esPort = 9300;
//    private String esClusterName = "yeekit-dev";
//
//
//    public TransportClient esClient()     {
//        Settings settings = Settings.settingsBuilder().put("cluster.name", esClusterName).build();
//        TransportClient client = TransportClient.builder().settings(settings).build();
//        try {
//            for (String host : esHosts.split(",")) {
//
//                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), esPort));
//            }
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return client;
//    }
//
//}
