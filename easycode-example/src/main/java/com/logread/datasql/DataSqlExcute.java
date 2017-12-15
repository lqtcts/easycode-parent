package com.logread.datasql;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 */
public class DataSqlExcute {
    private static final Log logger = LogFactory.getLog(DataSqlExcute.class);

    private static MysqlDataSource mysqlDataSource;
    private static ThreadLocal<Connection> t;

    static {
        mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl("jdbc:mysql://115.182.33.177/wins-dsp-new?user=root&password=pxene");
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            if (t == null) {
                t = new ThreadLocal<Connection>();
                conn = mysqlDataSource.getConnection();
                t.set(conn);
            } else if (t.get() == null || t.get().isClosed()) {
                conn = mysqlDataSource.getConnection();
                t.set(conn);
            } else {
                conn = t.get();
            }
        } catch (SQLException e) {
            logger.error("mysql get conn error :" + e);
        }
        return conn;
    }


    public static boolean updateToMysql(String sql) {
        Statement st = null;
        Connection conn = getConnection();
        try {
            st = conn.createStatement();
            if (st.executeUpdate(sql) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                logger.info("close mysql resource error :" + e);
            }
        }
    }

    public static boolean inserToMysql(String sql) {
        Statement st = null;
        Connection conn = getConnection();
        try {
            st = conn.createStatement();
            if (st.executeUpdate(sql) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                logger.info("close mysql resource error :" + e);
            }
        }
    }

    /**
     * 查询有的话就返回true
     *
     * @param sql
     * @return
     */
    public boolean getDataExist(String sql) {
        Statement st = null;
        Connection conn = getConnection();
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                System.out.println("close mysql resource error :" + e);
            }
        }
        return false;
    }

    public static String selectOnlyValue(String sql) {
        Statement st = null;
        Connection conn = getConnection();
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                System.out.println("close mysql resource error :" + e);
            }
        }
        return null;
    }


}
