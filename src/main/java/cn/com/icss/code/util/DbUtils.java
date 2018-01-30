package cn.com.icss.code.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * 描述：数据库信息类
 * 
 * @author joan.xiong
 * @version 1.0 2013-10-21
 */
public class DbUtils
{

	/**
	 * 数据库类型
	 */
	private String dataBaseType="oracle";
	
	Logger log = Logger.getLogger(DbUtils.class);

	/** 数据库中各列的名称(数据库中的名称) */
	private Map<String, String> tbColumnList = new LinkedHashMap<String, String>();

	/** 数据库中各列的名称(首字母小写) */
	private Map<String, String> lcolumnList = new LinkedHashMap<String, String>();

	/** 数据库中各列的名称(首字母大写) */
	private Map<String, String> ucolumnList = new LinkedHashMap<String, String>();

	/** 数据库中各列的类型的类名 */
	private Map<String, String> typeList = new LinkedHashMap<String, String>();

	/** 数据库中各列的类型名称 */
	private Map<String, String> jdbcList = new LinkedHashMap<String, String>();

	/** 数据库中各列的类型名称 */
	private Map<String, String> commentList = new LinkedHashMap<String, String>();
	public Map<String, String> getCommentList()
	{
		return commentList;
	}
	public void setCommentList(Map<String, String> commentList)
	{
		this.commentList = commentList;
	}

	/** 类前缀名称 */
	private String proxClassName;

	private String tableName;

	/** 表的主键 */
	private String pkColumn;

	DataSource dataSource;
	//页面列表显示列
	private String[] webListPageFields;
	//页面新增显示列
	private String[] webAddPageFields;
	//页面修改显示列
	private String[] webUpdatePageFields;
	//页面修改显示列
	private String[] webQueryConditionFields;

	/**
	 * 获取数据库连接
	 * 
	 * @return conn Connection
	 */
	public Connection getConnection()
	{
		Connection conn = null;
		InputStream in = ClassLoader.getSystemResourceAsStream("conf/properties/config.properties");
		Properties p = new Properties();
		try
		{
			p.load(in);
			String driverClassName=p.getProperty("driverClassName");
			if(driverClassName.indexOf("oracle")>-1){
				dataBaseType="oracle";
			}else if(driverClassName.indexOf("mysql")>-1){
				dataBaseType="mysql";
			}
			
			Class.forName(driverClassName);
			conn = DriverManager.getConnection(p.getProperty("jdbc_url"), p.getProperty("jdbc_username"), p.getProperty("jdbc_password"));
		}
		catch (SQLException e)
		{
			log.error("数据库连接异常", e);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally{
			try
			{
				if (in != null)
					in.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conn;
	}

	/**
	 * 设置Web页面要显示的字段
	 * @param p
	 */
	private void setWebPageFields(){
		InputStream in = ClassLoader.getSystemResourceAsStream("conf/properties/config.properties");
		Properties p = new Properties();
		
		try
		{
			p.load(in);
			String listFields = p.getProperty("web.listPage.FieldList");
			String addFields = p.getProperty("web.addPage.FieldList");
			String updateFields = p.getProperty("web.updatePage.FieldList");
			String queryFields = p.getProperty("web.queryCondition.FieldList");
			if (listFields != null && !"".equals(listFields))
				this.webListPageFields = listFields.trim().split(",");
			if (addFields != null && !"".equals(addFields))
				this.webAddPageFields = addFields.trim().split(",");
			if (updateFields != null && !"".equals(updateFields))
				this.webUpdatePageFields = updateFields.trim().split(",");
			if (queryFields != null && !"".equals(queryFields))
				this.webQueryConditionFields = queryFields.trim().split(",");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try
			{
				if (in != null)
					in.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 设置主键
	 */
	public void setPk()
	{
		Connection conn = getConnection();
		try
		{
			DatabaseMetaData dbMeta = conn.getMetaData();
			ResultSet pkRSet = dbMeta.getPrimaryKeys(null, null, tableName.toUpperCase());
			// pkRSet.next();
			// presently only support one column pk
			if (pkRSet.next())
			{
				pkColumn = pkRSet.getString("column_name");
			}
		}
		catch (SQLException e)
		{
			log.error("设置主键列异常", e);
		}
	}

	/**
	 * 获取数据库各列信息，
	 * 
	 * @param data
	 * @return
	 */
	public void setColumn()
	{
		// 先设置主键
		setPk();

		Connection conn = getConnection();
		String sql = "select * from " + tableName;
		PreparedStatement stmt;
		try
		{
			stmt = conn.prepareStatement(sql);
			stmt.execute(); // 这点特别要注意:如果是Oracle而对于mysql可以不用加.
			ResultSetMetaData data = stmt.getMetaData();
			for (int i = 1; i <= data.getColumnCount(); i++)
			{
				// 获得数据库中的列名
				String tbColumnName = data.getColumnName(i);
				// 将数据库的列名转换成文件类名称
				String columnName = data.getColumnName(i);
				System.out.println("columnName: " + columnName);
				columnName = formatString(columnName);

				System.out.println("columnName: " + columnName);

				// 主键
				if (pkColumn.equals(tbColumnName))
				{
					lcolumnList.put("id", columnName);
					ucolumnList.put("id", getUpperString(columnName));
					typeList.put("id", data.getColumnClassName(i));
					tbColumnList.put("id", tbColumnName);
					jdbcList.put("id", dbToJdbcType(data.getColumnTypeName(i), data.getScale(i)));
					
				}
				else
				{
					lcolumnList.put(columnName, columnName);
					ucolumnList.put(columnName, getUpperString(columnName));
					typeList.put(columnName, data.getColumnClassName(i));
					tbColumnList.put(columnName, tbColumnName);
					jdbcList.put(columnName, dbToJdbcType(data.getColumnTypeName(i), data.getScale(i)));
				}
			}
			
			sysoutMySqlTCloumns();
			//设置页面显示字段
			setWebPageFields();
		}
		catch (SQLException e)
		{
			log.error("获取各列信息异常", e);
		}
	}

	/***
	 * 打印MySql的表模板参数文件(jsp):
	 * 
	 * @throws SQLException
	 * @throws UnsupportedEncodingException
	 */
	public void sysoutMySqlTCloumns()
	{
		// getMySqlConnection();

		Connection con = getConnection();
		String sql = "select * from " + tableName;
		Statement stmt;

		// System.setProperty("file.encoding", "UTF-8");
		List<HashMap<String, String>> columns = new ArrayList<HashMap<String, String>>();
		try
		{
			stmt = con.createStatement();
			if(dataBaseType.equals("mysql")){
				sql = " show full columns from " + tableName.toUpperCase();
				System.out.println(sql);
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next())
				{
					HashMap<String, String> map = new HashMap<String, String>();
					String Comment = rs.getString("Comment");
					map.put("Name", Comment);
					map.put("Code", rs.getString("Field"));
					map.put("DataType", rs.getString("Type"));
					map.put("Comment", rs.getString("Comment")); // varchar int text
																	// datetime
					map.put("Primary", "YES".equals(rs.getString("Key")) ? "TRUE" : "FALSE");
					map.put("Mandatory", !"YES".equals(rs.getString("Null")) ? "TRUE" : "FALSE");
					columns.add(map);
					
					String columnName = rs.getString("Field");
					columnName=formatString(columnName);
					commentList.put(columnName, rs.getString("Comment"));
					System.out.println(columnName+": "+rs.getString("Comment"));
					
					// 有没搞错，关主键毛事，一定要加.
					System.out.println("主键: "+pkColumn);
					if(formatString(pkColumn).equals(columnName))
						commentList.put("id", rs.getString("Comment"));
				}
			}else if(dataBaseType.equals("oracle")){
				sql = "select ucc.comments,atc.COLUMN_NAME,atc.DATA_TYPE,atc.NULLABLE  from all_tab_columns atc left join user_col_comments ucc on  atc.TABLE_NAME=ucc.table_name and atc.COLUMN_NAME=ucc.column_name  where atc.table_name='" + tableName.toUpperCase()+"'  and ucc.comments is not null";
				
				System.out.println(sql);
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next())
				{
					HashMap<String, String> map = new HashMap<String, String>();
					String Comment = rs.getString("COMMENTS");
					String Field=rs.getString("COLUMN_NAME");
					map.put("Name", Comment);
					map.put("Code", Field);
					map.put("DataType", rs.getString("DATA_TYPE"));
					map.put("Comment", Comment); // varchar int text
																	// datetime
					//map.put("Primary", "YES".equals(rs.getString("Key")) ? "TRUE" : "FALSE");
					map.put("Mandatory", !"YES".equals(rs.getString("NULLABLE")) ? "TRUE" : "FALSE");
					columns.add(map);
					
					String columnName = Field;
					columnName=formatString(columnName);
					commentList.put(columnName, Comment);
					System.out.println(columnName+": "+Comment);
					
					// 有没搞错，关主键毛事，一定要加.
					System.out.println("主键: "+pkColumn);
					if(formatString(pkColumn).equals(columnName))
						commentList.put("id", Comment);
				}
			}
			
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 输出
		for (HashMap<String, String> map : columns)
		{
			String Name = map.get("Name");
			String Code = map.get("Code");
			String Comment = map.get("Comment");
			String DataType = map.get("DataType");
			String Primary = map.get("Primary");
			Name = Comment.split("\\s+")[0];
			String Mandatory = map.get("Mandatory");
			String str = "table.cols.add(new Column(\"" + Name + "\",\"" + Code + "\",\"" + Comment + "\",\"" + DataType + "\",\"" + Primary + "\",\"" + Mandatory + "\"));";
			System.out.println(str);
		}
		
		// 中文化生成.
		System.out.println("\n\n\n");
		System.out.println("---------------------------------------------");
		System.out.println("中文化生成");
		HashMap<String, String> messageMap=new HashMap<String, String>();
		for(String key:commentList.keySet())
		{
			System.out.println("##"+commentList.get(key));
			System.out.println(formatString(tableName)+"."+key+"="+UnicodeTranslate.gbEncoding(commentList.get(key)));
		}
		System.out.println("\n\n\n");
		
	}

	/**
	 * 转换数据库类型
	 * 
	 * @param str 类型名称
	 * @param scale 小数点位数
	 * @return type String
	 */
	private String dbToJdbcType(String str, int scale)
	{
		String type = str;
		if ("VARCHAR2".equals(str)){
			type = "VARCHAR";
		}else if ("DATETIME".equals(str)){
			type = "TIMESTAMP";
		}else if ("INT".equals(str)){
			type = "INTEGER";
		}else if ("NUMBER".equals(str)){
			if (scale <= 0)
				type = "INTEGER";
			else if (scale > 0)
				type = "DECIMAL";
		}else if ("LONGTEXT".equals(str)){
			type = "CLOB";
		}else if ("TEXT".equals(str)){
			type = "CLOB";
		}
		
		return type;
	}

	/**
	 * 并去掉（_）转换成驼峰方式
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	private String formatString(String str)
	{
		if (str == null)
		{
			return "";
		}
		StringBuffer sb = new StringBuffer();
		String[] arr = str.toUpperCase().split("_");
		for (int c = 0; c < arr.length; c++)
		{
			String temp = arr[c];
			if (c > 0)
			{
				sb.append(temp.substring(0, 1) + temp.substring(1).toLowerCase());
			}
			else
			{
				sb.append(arr[0].toLowerCase());
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符串首字母大写
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	private String getUpperString(String str)
	{
		if (str == null || "".equals(str))
		{
			return "";
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return DynamicDataSource
	 */
	public DataSource getDataSource()
	{
		return dataSource;
	}

	/**
	 * 设置数据库连接
	 * 
	 * @param dataSource
	 *            DynamicDataSource
	 */
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	/**
	 * 获取列首字母小写的map
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getLcolumnList()
	{
		return lcolumnList;
	}

	/**
	 * 设置列首字母小写map
	 * 
	 * @param lcolumnList
	 *            Map<String, String>
	 */
	public void setLcolumnList(Map<String, String> lcolumnList)
	{
		this.lcolumnList = lcolumnList;
	}

	/**
	 * 获取列大写首字母的map
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getUcolumnList()
	{
		return ucolumnList;
	}

	/**
	 * 设置列首字母大写的map
	 * 
	 * @param ucolumnList
	 *            Map<String, String>
	 */
	public void setUcolumnList(Map<String, String> ucolumnList)
	{
		this.ucolumnList = ucolumnList;
	}

	/**
	 * 获取表名称
	 * 
	 * @return String
	 */
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * 设置表名称
	 * 
	 * @param tableName
	 *            String
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * 获取数据库表中各字段名称
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getTbColumnList()
	{
		return tbColumnList;
	}

	/**
	 * 设置数据库表中各字段名称
	 * 
	 * @param tbColumnList
	 *            Map<String, String>
	 */
	public void setTbColumnList(Map<String, String> tbColumnList)
	{
		this.tbColumnList = tbColumnList;
	}

	/**
	 * 获取类型map
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getTypeList()
	{
		return typeList;
	}

	/**
	 * 设置类型map
	 * 
	 * @param typeList
	 *            Map<String, String>
	 */
	public void setTypeList(Map<String, String> typeList)
	{
		this.typeList = typeList;
	}

	/**
	 * 获取对应的jdbc类型map
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getJdbcList()
	{
		return jdbcList;
	}

	/**
	 * 设置对应的jdbc类型map
	 * 
	 * @param jdbcList
	 *            Map<String, String>
	 */
	public void setJdbcList(Map<String, String> jdbcList)
	{
		this.jdbcList = jdbcList;
	}

	/**
	 * 获取类前缀名称
	 * 
	 * @return String
	 */
	public String getProxClassName()
	{
		return proxClassName;
	}

	/**
	 * 设置类前缀名称
	 * 
	 * @param proxClassName
	 *            String
	 */
	public void setProxClassName(String proxClassName)
	{
		this.proxClassName = proxClassName;
	}

	/**
	 * 获取主键列
	 * 
	 * @return String
	 */
	public String getPkColumn()
	{
		return pkColumn;
	}

	/**
	 * 设置主键列
	 * 
	 * @param pkColumn
	 *            String
	 */
	public void setPkColumn(String pkColumn)
	{
		this.pkColumn = pkColumn;
	}
	public String getDataBaseType()
	{
		return dataBaseType;
	}
	public void setDataBaseType(String dataBaseType)
	{
		this.dataBaseType = dataBaseType;
	}
	public String[] getWebListPageFields()
	{
		return webListPageFields;
	}
	public void setWebListPageFields(String[] webListPageFields)
	{
		this.webListPageFields = webListPageFields;
	}
	public String[] getWebAddPageFields()
	{
		return webAddPageFields;
	}
	public void setWebAddPageFields(String[] webAddPageFields)
	{
		this.webAddPageFields = webAddPageFields;
	}
	public String[] getWebUpdatePageFields()
	{
		return webUpdatePageFields;
	}
	public void setWebUpdatePageFields(String[] webUpdatePageFields)
	{
		this.webUpdatePageFields = webUpdatePageFields;
	}
	public String[] getWebQueryConditionFields()
	{
		return webQueryConditionFields;
	}
	public void setWebQueryConditionFields(String[] webQueryConditionFields)
	{
		this.webQueryConditionFields = webQueryConditionFields;
	}
	
}
