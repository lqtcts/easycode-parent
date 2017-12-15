package com.stream.kafka.kafka2hdfs;

import com.google.common.collect.Maps;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HDFSWriter {

	private final Log logger = LogFactory.getLog(HDFSWriter.class);
    
    private static Calendar calendar;
    private static final DateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat sdf2 = new SimpleDateFormat("HHmmss");
    private static final String WRAP_SYMBOL = System.getProperty("line.separator", "\n");

    private FileSystem fileSystem;
    private FSDataOutputStream outputStream;
    private ExecutorService pool;
    private int flushBatchSize = 0;
    private int flushSize = 1000;
    private int currentMessages = 0;
    private int maximumMessagesInFile = 1000000;
    private String rootStr;
    private String currentDir;
    private String lastDate;
    private List<KafkaStream<byte[], byte[]>> kafkaStreams;

    private long lastTime;
    private long  currentTime;
    Timer timer = new Timer();
    private int timerInterval;
    /**
     * 设置配置文件
     * @param conf
     *          配置文件
     */
    private static void getConfiguration(Configuration conf){
        conf.set("fs.defaultFS", "hdfs://mycluster");
        conf.set("dfs.nameservices", "mycluster");
        conf.set("dfs.ha.namenodes.mycluster", "nn1,nn2");
        conf.set("dfs.namenode.rpc-address.mycluster.nn1", "10.0.0.30:9000");
        conf.set("dfs.namenode.rpc-address.mycluster.nn2", "10.0.0.34:9000");
        conf.set("dfs.client.failover.proxy.provider.mycluster",
                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
    }
    
    public void init(String topic, String root, String zk, String groupid, int threads,int Interval) {
		try {
			timerInterval= Interval;
			pool = Executors.newFixedThreadPool(threads);
			Config config = new Config();
			ConsumerConnector consumerConnector = config.consumerConnector(zk, groupid);
			initKafka(consumerConnector, topic);
			lastDate = getDate();
			rootStr = root;
			currentDir = root + "/" + lastDate;
			Path currentPath = new Path(currentDir + "/" + getTime());
			//初始化配置文件
	        Configuration conf = new Configuration();
            //获取配置信息
            getConfiguration(conf);
            fileSystem = FileSystem.get(conf);
//			fileSystem = currentPath.getFileSystem(config.configure());
			createIfNotExists(new Path(currentDir));
			deleteIfExists(currentPath);
			startWriting(currentPath);
			timeFlush();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
		}
    }
    /**
     * 定时写入
     */
    private void timeFlush(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	possiblyRotateFile();
                flushWritesIfNeeded();
            }
        }, 0, timerInterval);
    }
    
	private void initKafka(ConsumerConnector consumerConnector, String topic) {
        Map<String, Integer> topicsToThreads = Maps.newHashMap();
        topicsToThreads.put(topic, 1);
        kafkaStreams = consumerConnector.createMessageStreams(topicsToThreads).get(topic);
    }

    public void startWriting(Path currentPath) {
        try {
        	logger.info("Start Writing...");
			outputStream = fileSystem.create(currentPath);
			for (KafkaStream<byte[], byte[]> stream : kafkaStreams) {
				pool.submit(new StreamConsumer(this, stream));
			}
		} catch (IOException e) {
			logger.error("******startWriting error : " + e);
		}
    }

    private void rotateFile() {
        try {
			outputStream.hflush();
			Path rotatedPath = new Path(currentDir + "/" + getTime());
			outputStream.close();
			outputStream = fileSystem.create(rotatedPath);
			currentMessages = 0;
		} catch (IOException e) {
			logger.error("******rotateFile error : " + e);
		}
    }

    /**
     * 获取格式化日期作为文件夹名
     * @return
     */
    private String getDate() {
    	calendar = Calendar.getInstance();
		return sdf1.format(calendar.getTime());
	}

    /**
     * 获取格式化时间作为文件名
     * @return
     */
    private String getTime() {
    	calendar = Calendar.getInstance();
		return sdf2.format(calendar.getTime());
	}
    
    private void createIfNotExists(Path path) {
		try {
			if (!fileSystem.exists(path)) {
				fileSystem.mkdirs(path);
			}
		} catch (IOException e) {
			logger.error("******create dir error : " + e);
		}
	}
    
	private void deleteIfExists(Path path) {
        try {
			if (fileSystem.exists(path)) {
			    fileSystem.delete(path, true);
			}
		} catch (IOException e) {
			logger.error("******deleteIfExists error : " + e);
		}
    }

    public void write(String message) {
        try {
        	outputStream.write((message + WRAP_SYMBOL).getBytes());
			flushBatchSize++;
            possiblyRotateFile();
	        flushWritesIfNeeded();
		} catch (Exception e) {
			close();
			logger.error("******write error : " + e);
		}
    }

    private void possiblyRotateFile() {
    	try {
    		if (!isDateEquals()) {
    			currentDir = rootStr + "/" + lastDate;
    			createIfNotExists(new Path(currentDir));
    			rotateFile();
    			return;
    		}
			currentMessages++;
			if (currentMessages == maximumMessagesInFile) {
			    rotateFile();
			}
		} catch (IllegalArgumentException e) {
			logger.error("******possiblyRotateFile error : " + e);
		}
    }

    /**
     * 判断是否隔天
     * @return
     */
    private boolean isDateEquals() {
    	String newDate = getDate();
    	if (newDate.equals(lastDate)) {
			return true;
		}
    	lastDate = newDate;
		return false;
	}

	private void flushWritesIfNeeded() {
		try {
            currentTime = System.currentTimeMillis();
            if (flushBatchSize == flushSize || currentTime >= lastTime + timerInterval) {
                outputStream.hflush();
                flushBatchSize = 0;
                lastTime = currentTime;
            }
        } catch (IOException e) {
            logger.error("******flushWritesIfNeeded error : " + e);
        }
    }

    public void close() {
        try {
			outputStream.hflush();
			outputStream.close();
			pool.shutdown();
			fileSystem.close();
		} catch (IOException e) {
			logger.error("******close error : " + e);
		}
    }

}
