package cn.com.icss.code.model;

/**
 * 
 * velocity生成注释信息类
 *
 */
public class Annotation {

	/**
	 * 作者名称
	 */
	private String authorName;
	/**
	 * 作者邮箱
	 */
	private String authorMail;
	/**
	 * 日期
	 */
	private String date;
	/**
	 * 版本
	 */
	private String version;

	/**
	 * 获取作者姓名
	 * @return authorName
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * 设置作者姓名
	 * @param authorName String
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 * 获取作者邮箱
	 * @return authorMail String
	 */
	public String getAuthorMail() {
		return authorMail;
	}

	/**
	 * 设置作者邮箱
	 * @param authorMail String
	 */
	public void setAuthorMail(String authorMail) {
		this.authorMail = authorMail;
	}

	/**
	 * 获取时间
	 * @return date String（yyyy-mm-dd）
	 */
	public String getDate() {
		return date;
	}

	/**
	 * 设置时间
	 * @param date String（yyyy-mm-dd）
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * 获取版本号
	 * @return version String
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 设置版本号
	 * @param version String
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
