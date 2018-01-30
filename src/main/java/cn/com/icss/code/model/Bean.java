package cn.com.icss.code.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * velocity实体公共类
 * @author zhengz
 *
 */
public class Bean {

	/** 模块名称 */
	private String modelName;
	/** bean 首字母小写名称 */
	private String lowerName;
	/**bean首字母大写名称*/
	private String uperName;
	/** bean 路径 */
	private String beanUrl;
	/** dao 路径 */
	private String beanDaoUrl;
	/** dao 实现路径 */
	private String beanDaoImplUrl;
	/** service 路径 */
	private String beanServiceUrl;
	/** service 实现路径 */
	private String beanServiceImplUrl;

	/** 其他包引用的model路径*/
	private String impBeanUrl;
	
	/** 其他包引用的dao路径*/
	private String impDaoUrl;
	
	/** 其他包引用的servcie路径*/
	private String impBeanServcieUrl;
	
	/**数据库中各列的名称(首字母小写)*/
	private Map<String,String> lcolumnList;
	
	/**数据库中各列的名称(首字母大写)*/
	private Map<String,String> ucolumnList;
	
	/**数据库中各列的名称(数据库中的名称)*/
	private Map<String,String> tbColumnList;
	
	//页面列表显示列
	private String[] webListPageFields;
	//页面新增显示列
	private String[] webAddPageFields;
	//页面修改显示列
	private String[] webUpdatePageFields;
	//列表筛选条件
	private String[] webQueryConditionFields;
	
	private Map<String,String> commentList;
	public Map<String, String> getCommentList()
	{
		return commentList;
	}
	public void setCommentList(Map<String, String> commentList)
	{
		this.commentList = commentList;
	}
	
	

	/**数据库中各列的类型*/
	private Map<String,String> typeList ;
	
	/**数据库中各列的类型名称*/
	private Map<String,String> jdbcList ;
	
	
	/**数据库中各列的名称(数据库中的名称)*/
	
	/** 数据库表名称**/
	private String tableName;
	
	/**类前缀名称*/
	private String proxClassName;
	
	/**类实体中文名称*/
	private String proxClassZhName;
	
	//---------------------------------------------------------------
	//dao 首字母小写名称
	private String lDaoName;
	
	//service 首字母小写名称
	private String lServiceName;
	
	/**工程名称*/
	private String strTypeName;
	
	//父节点的属性
	private List<Map<String,String>> rootList = new ArrayList<Map<String,String>>();
	//select点的属性
	private List<Map<String,String>> selectList = new ArrayList<Map<String,String>>();
	//delete点的属性
	private List<Map<String,String>> deleteList = new ArrayList<Map<String,String>>();
	//insert点的属性
	private List<Map<String,String>> insertList = new ArrayList<Map<String,String>>();
	//update点的属性
	private List<Map<String,String>> updateList = new ArrayList<Map<String,String>>();
	//resultMap点的属性
	private List<Map<String,String>> resultMapList = new ArrayList<Map<String,String>>();

	/**
	 * 获取首字母小写名称
	 * @return lowerName String
	 */
	public String getLowerName() {
		return lowerName;
	}

	/**
	 * 设置首字母加小写名称
	 * @param lowerName String
	 */
	public void setLowerName(String lowerName) {
		this.lowerName = lowerName;
	}

	/**
	 * 设置bean的rul
	 * @return beanUrl String
	 */
	public String getBeanUrl() {
		return beanUrl;
	}

	/**
	 * 设置bean的URL
	 * @param beanUrl String
	 */
	public void setBeanUrl(String beanUrl) {
		this.beanUrl = beanUrl;
	}

	/**
	 * 获取dao的rul
	 * @return beanDaoUrl String
	 */
	public String getBeanDaoUrl() {
		return beanDaoUrl;
	}

	/**
	 * 设置dao的rul
	 * @param beanDaoUrl String
	 */
	public void setBeanDaoUrl(String beanDaoUrl) {
		this.beanDaoUrl = beanDaoUrl;
	}

	/**
	 * 获取dao实现的地址
	 * @return beanDaoImplUrl String
	 */
	public String getBeanDaoImplUrl() {
		return beanDaoImplUrl;
	}

	/**
	 * 设置dao实现的url
	 * @param beanDaoImplUrl String
	 */
	public void setBeanDaoImplUrl(String beanDaoImplUrl) {
		this.beanDaoImplUrl = beanDaoImplUrl;
	}

	/**
	 * 获取service的rul
	 * @return beanServiceUrl String
	 */
	public String getBeanServiceUrl() {
		return beanServiceUrl;
	}

	/**
	 * 设置service的rul
	 * @param beanServiceUrl String
	 */
	public void setBeanServiceUrl(String beanServiceUrl) {
		this.beanServiceUrl = beanServiceUrl;
	}

	/**
	 * 获取service实现的url
	 * @return beanServiceImplUrl String
	 */
	public String getBeanServiceImplUrl() {
		return beanServiceImplUrl;
	}

	/**
	 * 设置service实现的url
	 * @param beanServiceImplUrl String
	 */
	public void setBeanServiceImplUrl(String beanServiceImplUrl) {
		this.beanServiceImplUrl = beanServiceImplUrl;
	}

	/**
	 * 获取首字母大写的名称
	 * @return uperName String
	 */
	public String getUperName() {
		return uperName;
	}

	/**
	 * 设置首字母大写
	 * @param uperName String
	 */
	public void setUperName(String uperName) {
		this.uperName = uperName;
	}

	/**
	 * 获取service实现的url
	 * @return impBeanUrl String
	 */
	public String getImpBeanUrl() {
		return impBeanUrl;
	}

	/**
	 * 设置service实现的url
	 * @param impBeanUrl String
	 */
	public void setImpBeanUrl(String impBeanUrl) {
		this.impBeanUrl = impBeanUrl;
	}

	/**
	 * 获取service实现的url
	 * @return impBeanServcieUrl String
	 */
	public String getImpBeanServcieUrl() {
		return impBeanServcieUrl;
	}

	/**
	 * 设置service实现的url
	 * @param impBeanServcieUrl String
	 */
	public void setImpBeanServcieUrl(String impBeanServcieUrl) {
		this.impBeanServcieUrl = impBeanServcieUrl;
	}

	/**
	 * 获取小写的各列字段名
	 *   列明为key，列名小写为value的map
	 * @return lcolumnList Map<String,String>
	 */
	public Map<String,String> getLcolumnList() {
		return lcolumnList;
	}

	/**
	 * 设置小写的各列字段名
	 *    列明为key，列名小写为value的map
	 * @param lcolumnList Map<String,String>
	 */
	public void setLcolumnList(Map<String,String> lcolumnList) {
		this.lcolumnList = lcolumnList;
	}

	/**
	 * 设置首字母大写的各列字段名
	 * 
	 * @return ucolumnList Map<String,String>
	 */
	public Map<String,String> getUcolumnList() {
		return ucolumnList;
	}

	/**
	 * 设置各列名大写字段名
	 *     列名为key，各列大写为value的map
	 * @param ucolumnList Map<String,String>
	 */
	public void setUcolumnList(Map<String,String> ucolumnList) {
		this.ucolumnList = ucolumnList;
	}

	/**
	 * 获取dao实现的url
	 * @return impDaoUrl String
	 */
	public String getImpDaoUrl() {
		return impDaoUrl;
	}

	/**
	 * 设置dao实现的url
	 * @param impDaoUrl String
	 */
	public void setImpDaoUrl(String impDaoUrl) {
		this.impDaoUrl = impDaoUrl;
	}

	/**
	 * 获取 父节点的属性
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> getRootList() {
		return rootList;
	}

	/**
	 * 设置 父节点属性
	 * @param rootList List<Map<String, String>>
	 */
	public void setRootList(List<Map<String, String>> rootList) {
		this.rootList = rootList;
	}

	/**
	 * 获取 select点的属性
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> getSelectList() {
		return selectList;
	}

	/**
	 * 设置 select点的属性
	 * @param selectList List<Map<String, String>>
	 */
	public void setSelectList(List<Map<String, String>> selectList) {
		this.selectList = selectList;
	}

	/**
	 * 获取delete点的属性
	 * @return deleteList List<Map<String, String>>
	 */
	public List<Map<String, String>> getDeleteList() {
		return deleteList;
	}

	/**
	 * 设置delete点的属性
	 * @param deleteList List<Map<String, String>>
	 */
	public void setDeleteList(List<Map<String, String>> deleteList) {
		this.deleteList = deleteList;
	}

	/**
	 * 获取插入点的属性
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> getInsertList() {
		return insertList;
	}

	/**
	 * 设置插入点的属性
	 * @param insertList List<Map<String, String>>
	 */
	public void setInsertList(List<Map<String, String>> insertList) {
		this.insertList = insertList;
	}

	/**
	 * 获取更新点的属性
	 * @return updateList List<Map<String, String>>
	 */
	public List<Map<String, String>> getUpdateList() {
		return updateList;
	}

	/**
	 * 设置更新点属性
	 * @param updateList List<Map<String, String>>
	 */
	public void setUpdateList(List<Map<String, String>> updateList) {
		this.updateList = updateList;
	}

	/**
	 * 获取结果集list
	 * @return resultMapList List<Map<String, String>> 
	 */
	public List<Map<String, String>> getResultMapList() {
		return resultMapList;
	}

	/**
	 * 设置结果集list
	 * @param resultMapList List<Map<String, String>> 
	 */
	public void setResultMapList(List<Map<String, String>> resultMapList) {
		this.resultMapList = resultMapList;
	}

	/**
	 * 获取dao首字母小写名称
	 * @return lDaoName String
	 */
	public String getlDaoName() {
		return lDaoName;
	}

	/**
	 * 设置dao首字母小写名称
	 * @param lDaoName String
	 */
	public void setlDaoName(String lDaoName) {
		this.lDaoName = lDaoName;
	}

	/**
	 * 获取service首字母小写名称
	 * @return lServiceName String
	 */
	public String getlServiceName() {
		return lServiceName;
	}

	/**
	 * 设置service首字母小写名称
	 * @param lServiceName String
	 */
	public void setlServiceName(String lServiceName) {
		this.lServiceName = lServiceName;
	}

	/**
	 * 获取数据库中各字段的map
	 * @return tbColumnList Map<String, String>
	 */
	public Map<String, String> getTbColumnList() {
		return tbColumnList;
	}

	/**
	 * 设置数据库中各字段的map
	 * @param tbColumnList Map<String, String>
	 */
	public void setTbColumnList(Map<String, String> tbColumnList) {
		this.tbColumnList = tbColumnList;
	}

	/**
	 * 获取类型map
	 * @return typeList Map<String, String>
	 */
	public Map<String, String> getTypeList() {
		return typeList;
	}

	/**
	 * 设置类型map
	 * @param typeList Map<String, String>
	 */
	public void setTypeList(Map<String, String> typeList) {
		this.typeList = typeList;
	}

	/**
	 * 获取数据库表名称
	 * @return String
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置数据库表名称
	 * @param tableName String
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 获取数据库中类型map
	 * @return jdbcList Map<String, String>
	 */
	public Map<String, String> getJdbcList() {
		return jdbcList;
	}

	/**
	 * 设置数据库中类型map
	 * @param jdbcList Map<String, String>
	 */
	public void setJdbcList(Map<String, String> jdbcList) {
		this.jdbcList = jdbcList;
	}

	/**
	 * 获取类名前缀
	 * @return proxClassName String
	 */
	public String getProxClassName() {
		return proxClassName;
	}

	/**
	 * 设置类名前缀
	 * @param proxClassName String
	 */
	public void setProxClassName(String proxClassName) {
		this.proxClassName = proxClassName;
	}

	public String getProxClassZhName()
	{
		return proxClassZhName;
	}
	public void setProxClassZhName(String proxClassZhName)
	{
		this.proxClassZhName = proxClassZhName;
	}
	/**
	 * 获取模块名称
	 * @return modelName String
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * 设置模块名称
	 * @param modelName String
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * 获取工程类型名称
	 * @return String
	 */
	public String getStrTypeName() {
		return strTypeName;
	}

	/**
	 * 设置工程类型名称
	 * @param strTypeName String
	 */
	public void setStrTypeName(String strTypeName) {
		this.strTypeName = strTypeName;
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
