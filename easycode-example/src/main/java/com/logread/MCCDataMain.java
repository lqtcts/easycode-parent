package com.logread;

import com.logread.datainwork.DataSumInworkImpl;
import com.logread.datasql.CounterSettleDataSqlEx;
import com.logread.handle.CounterHandle;
import com.logread.handle.SettleHandle;
import com.logread.util.MccUtil;
import com.logread.util.DateUtil;
import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.logread.util.MccUtil.endDate;

public class MCCDataMain {

    public static String msg = "+~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+" +
                             "\n+参数解释:                                                                                   " +
                             "\n+     path:(日志文件路径 默认为/data1/log/dspads_odin/)                                        " +
                             "\n+     dataType:(要刷回什么类型的数据 默认为0全部)                                              " +
                             "\n+          1:imcl       :点击&展现                                                           " +
                             "\n+          2:cost       :花费                                                              " +
                             "\n+     dimension:(要刷回什么维度的数据 默认为0全部)                                            " +
                             "\n+          1:device     :设备平台和操作系统                                                 " +
                             "\n+          2:network    :网络类型和网络运营商                                               " +
                             "\n+          3:creative   :创意展现形式                                                       " +
                             "\n+          4:app        :app名称                                                             " +
                             "\n+          5:ip         :地域                                                               " +
                             "\n+          6:phonebrand :手机品牌                                                           " +
                             "\n+     startDate:(从哪天开始修复-包含 默认昨天 yyyy-MM-dd)                                     " +
                             "\n+     endDate:(从哪天开始修复-包含 默认昨天 yyyy-MM-dd)                                      " +
                             "\n+~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+";

    public  static String errorSub="发现错误:";
    public static void main(String[] args) {
        args=new String[1];
        args[0] ="C:\\Users\\wuchengbin.POWERXENE\\Desktop\\dspads_odin,,,2016-12-08,";

        long start = System.currentTimeMillis();
        try {
            if (args == null || args.length == 0) {
                System.out.println("请输入完整参数使用...");
                System.out.println("args ：\"path,dataType,dimension,startDate,endDate\"");
                System.out.println(msg);
                System.out.println("like this : \"/data1/log/dspads_odin/,0,10\" ... ...");
            } else {
                System.out.println("--startTime:" +DateUtil.getStringDate());
                if(exec(args)){
                    System.out.println("--complete time:" +DateUtil.getStringDate()+"/"+ (System.currentTimeMillis() - start) / 1000 + "s");

                }else{
                    System.out.println("--error find.........");
                }
            }
        } catch (Exception e) {
            System.out.println("error...");
            e.printStackTrace();
        }
    }

    private static boolean exec(String[] args) {
        //校验参数
        if (checkParam(args)) {
            System.out.println(getStartParam()+"\n\n-----开始进行日志读取.....");
            //获取两个日期之间的天数
            long days = DateUtil.getDays( endDate,MccUtil.startDate);
            ExecutorService executor = null;
            CounterSettleDataSqlEx csse = new CounterSettleDataSqlEx();
            for (int i = 0; i <= days; i++) {
                //遍历每一天下面的counter和settle
                try {
                    String nextDateString = DateUtil.getNextDateString(MccUtil.startDate, i);
                    System.out.print("----------处理"+nextDateString);
                    //数据结果
                    DataSumInworkImpl dataSumInwork = new DataSumInworkImpl();
                    String counterPathp = MccUtil.path + "/" + MccUtil.pathCounterSub + "/" + MccUtil.pathCounterSub + "_" + nextDateString;
                    String settlePathp = MccUtil.path + "/" + MccUtil.pathSettleSub + "/" + MccUtil.pathSettleSub + "_" + nextDateString;

                    //counter列表
                    File[] counterFiles = getFilesByPath(counterPathp, true);
                    //settle列表
                    File[] settleFiles = getFilesByPath(settlePathp, false);
                    if (settleFiles.length == 0 && counterFiles.length == 0){
                        System.out.println("----------没有发现文件");
                        continue;
                    }

                    //线程池
                    int threadNum = settleFiles.length + counterFiles.length;
                    executor = Executors.newFixedThreadPool(threadNum);

                    //提交任务
                    if (counterFiles != null) {
                        for (File file : counterFiles) {
                            executor.submit(new FileReadMain(file.getPath(), new CounterHandle(), dataSumInwork));
                        }
                    }
                    if (settleFiles != null) {
                        for (File file : settleFiles) {
                            executor.submit(new FileReadMain(file.getPath(), new SettleHandle(), dataSumInwork));
                        }
                    }
                    System.out.print("----------");
                    //等待线程执行
                    executor.shutdown();
                    executor.awaitTermination(threadNum, TimeUnit.DAYS);
                    System.out.print("----------");
//                    for (String s : dataSumInwork.sumMap.keySet()) {
//                        System.out.println(s+"---"+dataSumInwork.sumMap.get(s));
//                    }
                    csse.insertOrUpdateDataMcc( dataSumInwork.sumMap);
                    System.out.print("----------successful");
                    System.out.println();
                } catch (InterruptedException e) {
//                  e.printStackTrace();
                    System.out.print("----------error:"+e.getMessage());
                }
            }
            return true;
        }else {
            return false;
        }

    }

    private static File[] getFilesByPath(String counterPathp, boolean counterFlag) {
        File[] files = new File[0];
        if (counterFlag) {
            if (MccUtil.DATATYPE == 0 || MccUtil.DATATYPE == MccUtil.IMCLI) {
                File dic = new File(counterPathp);
                files = dic.listFiles();
            }
        } else {
            if (MccUtil.DATATYPE == 0 || MccUtil.DATATYPE == MccUtil.COST) {
                File dic = new File(counterPathp);
                files = dic.listFiles();
            }
        }
        if (files == null){
            files = new File[0];
        }
        return files;
    }


    /**
     * 验证输入参数
     *
     * @param args
     * @return
     */
    private static boolean checkParam(String[] args) {
        String arg = args[0];

        if (arg == null|| "".equals(arg)) {
            MccUtil.startDate = DateUtil.getNextDateStringFormat(DateUtil.getStringDateShort(), -1);
            endDate = MccUtil.startDate;
        } else {
            //参数验证
            if (arg.endsWith(",")){
                arg=arg+"test";
            }
            String[] split = arg.split(",");

            if (split.length < 4) {
                System.out.println("参数不正确。。。不传递的参数可以不写，但是需要逗号");
                return true;
            } else {
                //path
                if (split[0] != null && !"".equals(split[0])) {
                    System.out.println("+~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ +");
                    System.out.println("+-------------------自定义目录请确保目录结构如下否则将不能进行解析                                +" +
                            "\n+-----userPath                                                                             +" +
                            "\n+--------dsp_impclk                                                                        +" +
                            "\n+-----------dsp_impclk_yyyyMMdd                                                            +" +
                            "\n+---------------logFile                                                                    +" +
                            "\n+--------dsp_settle                                                                        +" +
                            "\n+-----------dsp_settle_yyyyMMdd                                                            +" +
                            "\n+---------------logFile                                                                    +" +
                            "\n+~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+");
                    System.out.println();
                    MccUtil.path = split[0];
                }
                //datetype
                if (split[1] != null && !"".equals(split[1])) {
                    try {
                        MccUtil.DATATYPE = Math.abs(Integer.parseInt(split[1]));
                        if (MccUtil.DATATYPE>2){
                            throw  new Exception("取值不正确");
                        }
                    } catch (Exception e) {
                        System.out.println(errorSub+"参数dataType 不正确:dataType="+split[1]+" \n" + msg);
                        return false;
                    }
                }
                //dimension
                if (split[2] != null && !"".equals(split[2])) {
                    try {
                        MccUtil.DIMENSION = Math.abs(Integer.parseInt(split[2]));
                        if (MccUtil.DIMENSION>6){
                            throw  new Exception("取值不正确");
                        }
                    } catch (Exception e) {
                        System.out.println(errorSub+"参数dimension 不正确:dimension="+split[2]+" \n" + msg);
                        return false;
                    }
                }
                //startDate
                if (split[3] != null && !"".equals(split[3])) {
                    Date date = DateUtil.strToDate(split[3]);
                    if (date == null) {
                        System.out.println(errorSub+"参数 start 格式不正确:startDate="+split[3]+" \n" + msg);
                        return false;
                    }
                    MccUtil.startDate = split[3];
                } else {
                    MccUtil.startDate = DateUtil.getNextDateStringFormat(DateUtil.getStringDateShort(), -1);
                }
                //endDate
                if (split.length == 5) {
                    if (split[4] != null && !"test".equals(split[4])) {
                        String s = MccUtil.dateFormat(split[4]);
                        if (s == null) {
                            System.out.println(errorSub+"参数 endDate 格式不正确:endDate="+split[4]+" \n" + msg);
                            return false;
                        }
                        endDate = split[4];
                    } else {
                        endDate = DateUtil.getNextDateStringFormat(DateUtil.getStringDateShort(), -1);

                    }
                } else {
                    endDate = DateUtil.getNextDateStringFormat(DateUtil.getStringDateShort(), -1);

                }
            }
            if (DateUtil.getDays(MccUtil.startDate,DateUtil.getStringDateShort())>-1){
                System.out.println(errorSub+"日期不能超过昨天，当天不能使用该工具，以免影响当日数据");
            }

            long days = DateUtil.getDays( endDate,MccUtil.startDate);
            if (days < 0) {
                System.out.println("日期区间不正确");
                return false;
            }
        }

        return true;
    }

    public static String getStartParam(){
        String a = "\n处理参数如下：" +
                "\n--path:" + MccUtil.path + "" +
                "\n--dataType：" + MccUtil.DATATYPE +
                "\n--dimension:" + MccUtil.DIMENSION +
                "\n--startDate:" + MccUtil.startDate +
                "\n--endDate:" + MccUtil.endDate;

        return a;
    }
}
