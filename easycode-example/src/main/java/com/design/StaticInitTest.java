package com.design;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化的优雅实现
 * 可以在static处调用，
 * 也可以在普通方法里调用，都保证只初始化一次
 * 
 * 当然将enum块的代码直接放到StaticInitTest类的private static 方法里做也是可以的
 *
 * @author yzl
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class StaticInitTest {
    private static List<Integer> dataList = null;
    
    static{
        dataList = Singleton.INSTANCE.init();
    }
    
    /**
     * 
     * 单例模式来填充数据
     *
     * @author yzl
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    private static enum Singleton {
        INSTANCE;
        private List<Integer> list;
        
        private Singleton(){
            fillData();
        }
        /**
         * 
         * 初始化数据
         *
         * @see [相关类/方法](可选)
         * @since [产品/模块版本](可选)
         */
        private void fillData(){
            list = new ArrayList<Integer>(5);
            for(int i =1; i<6; i++){
                list.add(i);
            }
        }

        /**
         * 
         * 初始化的入口
         *
         * @see [相关类/方法](可选)
         * @since [产品/模块版本](可选)
         */
        public List<Integer> init(){
            return list;
        }
    }
}