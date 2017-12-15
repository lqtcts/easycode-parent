package com.utils;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MysqlUtil {
	private static final Log logger = LogFactory.getLog(MysqlUtil.class);
	
    private static MysqlDataSource mysqlDataSource;
    private static ThreadLocal<Connection> t;
    
    static {
    	mysqlDataSource = new MysqlDataSource();
//    	mysqlDataSource.setUrl("jdbc:mysql://192.168.3.166:3306/wins-dsp-new?user=root&password=pxene");
    	mysqlDataSource.setUrl("jdbc:mysql://10.0.0.22:3306/wins-dsp-new?user=root&password=pxene");
    }
    
	private static Connection getConnection() {
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
    
	public static void insert(String word, String deviceid) {  
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "INSERT INTO tmp(bid, deviceid)"  
                    + " VALUES ('"+word+"','"+deviceid+"')";
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	logger.info("Add record to mysql error :" + e.getMessage());  
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
    }  
	 
    public static void insertSettle(String bid, Long time, BigDecimal price) {  
    	Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "INSERT INTO settle_tmp(bid, time, price)"  
                    + " VALUES ('" + bid + "','" + time + "','" + price + "')";  
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	logger.info("Add record to mysql error :" + e);  
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
    }  
    
    public static void insertMotion(String bid, Long time, String type) {  
    	Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "INSERT INTO motion_tmp(bid, time, type)"  
                    + " VALUES ('" + bid + "','" + time + "','" + type + "')";  
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	logger.info("Add record to mysql error :" + e);  
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
    }
    
    public static String queryRecord(String appid) {  
    	Connection conn = getConnection();
    	Statement st = null;
    	ResultSet rs = null;
        try {  
            String sql = "SELECT app.name, app.path FROM  dsp_t_app_path app WHERE app.id = " + appid;  
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
				return rs.getString(1);
			}
        } catch (SQLException e) {  
        	logger.error("sql exception :" + e);
        } finally {
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
				logger.error("close mysql statement error :" + e);
			}
		}
        return null;
    }

    /**
     * storm对数
     * @param creativeid
     * @param show
     * @param click
     * @param category
     */
	public static void insertCount(String creativeid, int show,
			int click, int category) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "INSERT INTO count_tmp(creativeid, show_, click, category)"  
                    + " VALUES ('" + creativeid + "'," + show + "," + click + "," + category + ")"; 
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	logger.info("Add record to mysql error :" + e);  
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}  
	
	/**
	 * 更新小时汇总数据
	 * @param id
	 * @param nun
	 */
	public static void updateHourStatisCount(String id, String num) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "UPDATE dsp_t_statistics_hour SET totalcount = " + num  + " WHERE id = " + id; 
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}  
	
	/**
	 * 小时汇总表添加记录
	 * @param time
	 * @param groupid
	 * @param campaignid
	 * @param creativeid
	 * @param type
	 * @param category
	 * @param totalcount
	 */
	public static void insertHourStatisCount(String time, String groupid, String campaignid,
			String creativeid, String type, String category, String totalcount) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "INSERT INTO dsp_t_statistics_hour(time, groupid, campaignid, creativeid, type, category, totalcount)"  
                    + " VALUES ('" + time + "','" + groupid + "','" + campaignid + "','" + creativeid + "','" + type + "'," 
            		+ category + "," + totalcount + ")"; 
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}  
    
	/**
	 * 查询记录
	 * @param time
	 * @param creativeid
	 * @param type
	 * @param category
	 * @return 
	 */
	public static String selectHourRecord(String time, String creativeid, String type, String category) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "SELECT id FROM dsp_t_statistics_hour WHERE time = '" + time + "' AND creativeid = '"
            		+ creativeid + "' AND type = '" + type + "' AND category = " +  category + " LIMIT 1"; 
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
				return rs.getString(1);
			}
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
		return null;
	}

	/**
	 * 根据mapid查询groupid， campaignid
	 * @param mapid
	 * @return
	 */
	public static Map<String, String> queryGroupidCampaignidByMapid(String mapid) {
		Connection conn = getConnection();
    	Statement st = null;
        try {  
            String sql = "SELECT groupid, campaignid FROM dsp_t_statistics_hour WHERE creativeid = '" + mapid + "'";  
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
            	Map<String, String> res = new HashMap<String, String>();
				String groupid = rs.getString(1);
				String campaignid = rs.getString(2);
				res.put("groupid", groupid);
				res.put("campaignid", campaignid);
				return res;
			}
        } catch (SQLException e) {  
        	logger.error("sql exception :" + e);
        } finally {
			try {
				st.close();
			} catch (SQLException e) {
				logger.error("close mysql statement error :" + e);
			}
		}
        return null;
	}

	/**
	 * 更新结算数据
	 * @param time
	 * @param category
	 * @param mapid
	 * @param cost
	 */
	public static void updateHourStatisCost(String time, String category, String mapid, Double cost) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "UPDATE dsp_t_statistics_hour SET totalcost = " + cost  + " WHERE time = '" + time 
            		+ "' AND category = '" + category + "' AND creativeid = '" + mapid + "'"; 
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}
	
//-----------------------------------------------------------------------------------------
	public static int selectHourRecordNew(String time, String creativeid,
			int category, int flag) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "SELECT count(*) sum FROM dsp_t_statis_by_hour WHERE time = '" + time + "' AND creativeid = '"
            		+ creativeid + "' AND category = '" +  category + "' AND flag = '" + flag + "'"; 
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
				return rs.getInt(1);
			}
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
		return 0;
	}
	
	public static Map<String, Object> selectHourRecord(String time, String creativeid,
			int category, int flag) {
		Map<String, Object> result = new HashMap<String, Object>();
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "SELECT imprs,clks,cost sum FROM dsp_t_statis_by_hour WHERE time = '" + time + "' AND creativeid = '"
            		+ creativeid + "' AND category = " +  category + " AND flag = " + flag; 
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql); 
            int count = 0;
            while (rs.next()) {
            	count ++ ;
				int imprs = rs.getInt(1);
				int clks = rs.getInt(2);
				BigDecimal cost = rs.getBigDecimal(3);
				result.put("imprs", imprs);
				result.put("clks", clks);
				result.put("cost", cost);
				if(count > 1){
					throw new NullPointerException();
				}
			}
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
		return result;
	}
	
	public static void updateHourStatisCountNew(String time, String mapid, int category,
			int flag, String imprs, String clks) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "UPDATE dsp_t_statis_by_hour SET imprs = " + imprs  + ", clks = " + clks + " WHERE time = '" + 
            		time + "' AND creativeid = '" + mapid + "' AND category = " + category + " AND flag = " + flag; 
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}

	public static void insertHourStatisCountNew(String time, String mapid,
			int category, int flag, String imprs, String clks) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "INSERT INTO dsp_t_statis_by_hour(time, creativeid, category, flag, imprs, clks)"  
                    + " VALUES ('" + time + "','" + mapid + "'," + category + "," + flag + "," + imprs + "," + clks + ")"; 
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}  
	public static Map<String,String> getOutDateMapid() {
		Statement st = null;
		Connection conn = getConnection();
		Map<String,String> map = new HashMap<String, String>();
		try {  
			String sql = "SELECT"+
					" t.`id`,t1.`startdate`,t1.`enddate`" + 
					" FROM dsp_t_ad_group_creative t"+
					" LEFT JOIN dsp_t_ad_group t1 ON t.`groupid` = t1.`id`"+
					" WHERE t1.`startdate` IS NOT NULL"+
					" GROUP BY t.`id`";
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
//				list.add(rs.getString(1));
				map.put(rs.getString(1), rs.getString(2)+"_"+rs.getString(3));
			}
		} catch (SQLException e) {  
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (Exception e) {
				logger.info("close mysql resource error :" + e);  
			}
		}
		return map;
	}  
	
	public static void updateHourStatisCostNew(String time, String mapid, int category, int flag, Double cost) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "UPDATE dsp_t_statis_by_hour SET cost = " + cost  + " WHERE time = '" + 
            		time + "' AND creativeid = '" + mapid + "' AND category = " + category + " AND flag = " + flag;  
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}
	
	public static void insertHourStatisCostNew(String time, String creativeid,
			int category, int flag, Double cost) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "INSERT INTO dsp_t_statis_by_hour(time, creativeid, category, flag, cost)"  
                    + " VALUES ('" + time + "','" + creativeid + "'," + category + "," + flag + "," + cost + ")"; 
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}  
	
	public static void hourStatis() {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "SELECT DATE_FORMAT(t.time, '%Y-%m-%d') time, t.creativeid, t.category, SUM(t.imprs) imprs," 
            		+ "SUM(t.clks) clks, SUM(t.cost) cost, SUM(t.downloads) downloads, SUM(t.regists) regists FROM"
            		+ " dsp_t_statis_by_hour t GROUP BY DATE_FORMAT(t.time, '%Y-%m-%d 00:00:00'), t.creativeid, t.category"; 
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
            	insertDayStatisRecord(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4),
            			rs.getInt(5), rs.getDouble(6), rs.getInt(7), rs.getInt(8));
			}
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}
	
	public static void insertDayStatisRecord(String time, String creativeid,
			String category, int imprs, int clks, Double cost, int downloads, int regists) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "INSERT INTO dsp_t_statis_by_day(time, creativeid, category, imprs, clks, cost, downloads, regists)"  
                    + " VALUES ('" + time + "','" + creativeid + "','" + category + "'," + imprs + "," + clks + "," + cost
                    + "," + downloads + "," + regists + ")"; 
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}

	public static List<Map<String, Object>> queryAll() {
		Statement st = null;
		Connection conn = getConnection();
		List<Map<String, Object>> res = new ArrayList<Map<String,Object>>();
        try {  
            String sql = "SELECT time, creativeid, type, category, totalcount, totalcost FROM dsp_t_statistics_hour"; 
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
            	Map<String, Object> map = new HashMap<String, Object>();
            	map.put("time", rs.getObject(1));
            	map.put("creativeid", rs.getObject(2));
            	map.put("type", rs.getObject(3));
            	map.put("category", rs.getObject(4));
            	map.put("count", rs.getObject(5));
            	map.put("cost", rs.getObject(6));
				res.add(map);
			}
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
		return res;
	}

	public static Map<String, Object> selectHourRecordMap(String time, String creativeid, String category) {
		Statement st = null;
		Connection conn = getConnection();
		Map<String, Object> map = new HashMap<String, Object>();
        try {  
            String sql = "SELECT imprs, clks, cost FROM dsp_t_statis_by_hour WHERE time = '" + time 
            		+ "' AND creativeid = '" + creativeid + "' AND category = " +  category + " LIMIT 1";  
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
            	map.put("imprs", rs.getObject(1));
            	map.put("clks", rs.getObject(2));
            	map.put("cost", rs.getObject(3));
			}
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
		return map;
	}

	public static void updateHourStatis(String id, int count, BigDecimal cost) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "UPDATE dsp_t_statistics_hour SET totalcount = " + count  + ", totalcost = " + cost + " WHERE id = " + id + ""; 
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}

	public static void insertHourStatis(String time, String groupid,
			String campaignid, String creativeid, String type, String category,
			int count, BigDecimal cost) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "INSERT INTO dsp_t_statistics_hour(time, groupid, campaignid, creativeid, type, category, totalcount, totalcost)"  
                    + " VALUES ('" + time + "','" + groupid + "','" + campaignid + "','" + creativeid + "','" + type + "'," 
            		+ category + "," + count + "," + cost + ")"; 
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}
	
	public static void insertHourStatisNew(String time, String creativeid,
			String category, int imprs, int clks, BigDecimal cost) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "INSERT INTO dsp_t_statis_by_hour(time, creativeid, category, imprs, clks, cost)"  
                    + " VALUES ('" + time + "','" + creativeid + "','" + category + "'," + imprs + "," 
            		+ clks + "," + cost + ")"; 
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}

	public static void updateHourStatisNew(int imprs, int clks, BigDecimal costs,
			String time, String creativeid, String category) {
		Statement st = null;
		Connection conn = getConnection();
        try {  
            String sql = "UPDATE dsp_t_statis_by_hour SET imprs = " + imprs  + " , clks = " + clks + 
            		" , cost = " + costs + " WHERE time = '" + time + "' AND creativeid = '" +  creativeid
            		+ "' AND category = '" +  category + "'";  
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
	}  
	
	
	public static List<Map<String, Object>> getRepeatData(){
		List<Map<String, Object>>  result = new ArrayList<Map<String,Object>>();
		String sql = "select * from (select count(*) as c ,time,creativeid,category,flag from dsp_t_statis_by_hour "
				+ "GROUP BY time,creativeid,category,flag) as t where t.c > 1";
		Statement st = null;
		Connection conn = getConnection();
        try {  
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
            	Map<String, Object> map = new HashMap<String, Object>();
            	map.put("c", rs.getInt(1));
            	map.put("time", rs.getString(2));
            	map.put("creativeid", rs.getString(3));
            	map.put("category", rs.getInt(4));
            	map.put("flag", rs.getInt(5));
            	result.add(map);
			}
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
		return result;
	}
	
	public static List<Map<String, Object>> getRepeatData(String time,String creativeid,int category,int flag){
		List<Map<String, Object>>  result = new ArrayList<Map<String, Object>>();
		String sql = "SELECT createtime,imprs,clks,cost FROM dsp_t_statis_by_hour WHERE time = '" + time + "' AND "
				+ "creativeid = '" + creativeid + "' AND "
				+ "category = " + category + " and flag = " + flag + " order by createtime,imprs,clks,cost asc ";
		Statement st = null;
		Connection conn = getConnection();
        try {  
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
            	String createtime = rs.getString(1);
            	int imprs = rs.getInt(2);
            	int clks = rs.getInt(3);
            	String cost = rs.getString(4);
            	Map<String, Object> map = new HashMap<String, Object>();
            	map.put("createtime", createtime);
            	map.put("imprs", imprs);
            	map.put("clks", clks);
            	map.put("cost", cost);
            	result.add(map);
			}
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
		return result;
	}
	
	public static int getCount(String time,String creativeid,int category,int flag,String createtime,int imprs,int clks, String cost){
		String sql = "SELECT count(*) FROM dsp_t_statis_by_hour WHERE time = '" + time + "' "
				+ " AND creativeid = '" + creativeid + "' AND category = " + category + " AND "
						+ "flag = " + flag + " and createtime = '" + createtime + "' and imprs = " + imprs + " and clks = " + clks + " and cost = '" + cost + "'"
								+ " ORDER BY createtime,imprs,clks,cost asc ";
		Statement st = null;
		Connection conn = getConnection();
		int c = 0;
        try {  
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
            	c = rs.getInt(1);
            }
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
		return c;
	}
	
	public static int deleteRepeatData(String time,String creativeid,int category,int flag,String createtime, int imprs, int clks, String cost){
		String sql = "DELETE FROM dsp_t_statis_by_hour WHERE time = '" + time + "' AND"
				+ " creativeid = '" + creativeid + "' AND "
				+ "category = " + category + " and flag = " + flag + "  AND createtime = '" + createtime + "' and imprs=" + imprs + " and clks=" + clks+ " and cost = '" + cost + "'";
		Statement st = null;
		Connection conn = getConnection();
		int c = 0;
        try {  
            st = conn.createStatement();
            c = st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
		return c;
	}
	
	public static int insertData(String time,String creativeid,int category,int imprs,int clks,String cost,int flag,String createtime){
		String sql = "INSERT INTO dsp_t_statis_by_hour (`time`, `creativeid`, `category`, `imprs`, `clks`, `cost`, `downloads`, "
				+ "`regists`, `flag`, `createtime`) "
				+ "VALUES ('" + time + "', '" + creativeid + "', '" + category + "', '" + imprs + "', '" + 
				clks + "', '" + cost + "', '0', '0', '" + flag + "', '" + createtime + "')";
		Statement st = null;
		Connection conn = getConnection();
		int c = 0;
        try {  
            st = conn.createStatement();
            c = st.executeUpdate(sql);
        } catch (SQLException e) {  
        	e.printStackTrace();
        } finally {
        	try {
				st.close();
			} catch (SQLException e) {
				logger.info("close mysql resource error :" + e);  
			}
        }
        return c;
	}
	
	public static List<String> getAllCreativeid(String name) throws Exception{
		String sql = "select t.id from dsp_t_ad_group_creative t left join dsp_t_ad_group t1 "
				+ " on t.groupid = t1.id left join dsp_t_campaign t2 on t2.id = t1.campaignid "
				+ " where t2.name='" + name + "'";
		Connection conn = getConnection();
		List<String> result = new ArrayList<String>();
		PreparedStatement st = null; 
        st = conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
        	String id = rs.getString(1);
        	result.add(id);
		}
		rs.close();
		st.close();
		conn.close();
		return result;
	}
	/**
	 * 修复app定向信息为“全部”的推广组定向信息
	 * 
	 */
	public static void repairAppTarget(){
		try {
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
			Connection conn1 = getConnection();
			Statement stmt1 = conn1.createStatement();
			Connection conn2 = getConnection();
			Statement stmt2 = conn2.createStatement();
			Connection conn3 = getConnection();
			Statement stmt3 = conn3.createStatement();
			Connection conn4 = getConnection();
			Statement stmt4 = conn4.createStatement();
			//查询出定向为‘全部’即‘000000’的推广组
			String str = "SELECT t1.`groupid` FROM dsp_t_app_targeting_bak t LEFT JOIN dsp_t_ad_group_targeting t1 ON t.`grouptargetingid` = t1.`id`"+
			"WHERE t1.`targetingtype`='000014' AND t.`appid` = '000000' GROUP BY t1.`groupid`";
			ResultSet ids = stmt.executeQuery(str);
			while(ids.next()){
				String id = ids.getString(1);
				//查询推广组选择了的媒体
				String str1 = "select t.`adxid` from dsp_t_group_prelease_adx t where groupid ='"+id+"'";
				ResultSet adxids = stmt1.executeQuery(str1);
				//查询出推广组app定向表中的id，定向关联id
				String str2 = "SELECT t1.`id`,t1.`grouptargetingid` FROM dsp_t_ad_group_targeting t LEFT JOIN dsp_t_app_targeting t1 ON t.`id` = t1.`grouptargetingid`"
						+ " WHERE t.`targetingtype` ='000014' AND t.`groupid`='"+id+"'";
				ResultSet apps = stmt2.executeQuery(str2);
				while(apps.next()){
					String aid = apps.getString(1);
					String atid = apps.getString(2);
					while(adxids.next()){
						String adxid = adxids.getString(1);
						//将关联id等数据重新插入数据库，并且添加adxid
						String lastsql = "insert into `dsp_t_app_targeting_bak` (`id`, `grouptargetingid`, `appid`, `adxid`) "+
						"VALUES('"+UUID.randomUUID()+"','"+atid+"','000000','"+adxid+"');";
						stmt3.execute(lastsql);
					}
					//删除原来的adxid为空的数据
					String delsql = "delete FROM dsp_t_app_targeting_bak WHERE id = '"+aid+"'";
					stmt4.execute(delsql);
				}
			}
			System.out.println("=============>全部修改完毕！");
			stmt4.close();
			conn4.close();
			stmt3.close();
			conn3.close();
			stmt2.close();
			conn2.close();
			stmt1.close();
			conn1.close();
			stmt.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
