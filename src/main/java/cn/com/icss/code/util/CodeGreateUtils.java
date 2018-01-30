package cn.com.icss.code.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import cn.com.icss.code.model.Annotation;
import cn.com.icss.code.model.Bean;

/**
 * 描述：velocity生成文件工具类，代码自动生成工具
 * 
 * @author zhou.zheng
 * @version 1.0 2013-10-22
 */
public class CodeGreateUtils
{

	Logger log = Logger.getLogger(CodeGreateUtils.class);
	// vm文件路径
	private String baseVmPath = "conf/template";

	// 根包路径cn.com.gameco.taskTransfer
	private String basePath = "cn/com/icss";
	
	//是否有国际化
	private String isHaveI18n="true";

	// 跟包名
	private String basePackage = "cn.com.icss";

	// 工程路径
	private String webPath = System.getProperty("user.dir");
	// JAVA源文件目录
	private String javaFolder = "src";
	// Web文件目录
	private String webFolder = "webContent";

	private String javaSource = System.getProperty("user.dir") + File.separator + "src_source";
	private String webSource = System.getProperty("user.dir") + File.separator + "web_source";

	private Bean bean;
	private Annotation annotation;
	private String point = ".";

	private static String mapper = "Mapper";

	private static String service = "Service";

	private String ymd = "yyyy-MM-dd";
	
	private String listHTMLVm = "list.vm";
	private String listJSVm = "";

	public CodeGreateUtils()
	{
		InputStream in = ClassLoader.getSystemResourceAsStream("conf/properties/config.properties");
		Properties p = new Properties();
		try
		{
			p.load(in);
			if (!StringUtils.isEmpty(p.getProperty("gen.path.baseVm")))
			{
				baseVmPath = p.getProperty("gen.path.baseVm");
				isHaveI18n=p.getProperty("isHaveI18n");
				
			}
			if (!StringUtils.isEmpty(p.getProperty("gen.path.basePath")))
			{
				basePath = p.getProperty("gen.path.basePath");
			}
			if (!StringUtils.isEmpty(p.getProperty("gen.path.basePackage")))
			{
				basePackage = p.getProperty("gen.path.basePackage");
			}
			if (!StringUtils.isEmpty(p.getProperty("gen.path.project")))
			{
				webPath = p.getProperty("gen.path.project");
			}
			if (!StringUtils.isEmpty(p.getProperty("gen.path.javaFolder")))
			{
				javaFolder = p.getProperty("gen.path.javaFolder");
			}
			if (!StringUtils.isEmpty(p.getProperty("gen.path.webFolder")))
			{
				webFolder = p.getProperty("gen.path.webFolder");
			}
			if (!StringUtils.isEmpty(p.getProperty("gen.listHTMLVm.file")))
			{
				this.listHTMLVm = p.getProperty("gen.listHTMLVm.file");				
			}
			if (!StringUtils.isEmpty(p.getProperty("gen.listJSVm.file")))
			{
				this.listJSVm = p.getProperty("gen.listJSVm.file");
			}
		}
		catch (IOException e)
		{
		}
	}

	/**
	 * 初始化bean和注解
	 */
	public void init()
	{
		// 实体名称
		bean.setUperName(bean.getProxClassName().substring(0, 1).toUpperCase() + bean.getProxClassName().substring(1));
		// 实体全称
		StringBuffer cSb = new StringBuffer();
		cSb.append(basePackage).append(point).append(bean.getModelName()).append(point).append(bean.getUperName());

		String cName = cSb.toString();// basePackage + "." + bean.getModelName()
										// + "."+ bean.getUperName();
		bean.setBeanUrl(cName);
		bean.setLowerName(getLowercaseChar(getLastChar(cName)));

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ymd);
		annotation.setDate(simpleDateFormat.format(new Date()));
		annotation.setVersion("1.0");

		// 设置路径
		// bean的路径
		StringBuffer bSb = new StringBuffer();
		bSb.append(basePackage).append(".model.").append(bean.getModelName()).append(point).append(bean.getUperName());
		bean.setImpBeanUrl(bSb.toString());
		// dao的路径
		StringBuffer dSb = new StringBuffer();
		dSb.append(basePackage).append(".dao.").append(bean.getModelName()).append(point).append(bean.getUperName()).append(mapper);
		bean.setImpDaoUrl(dSb.toString());

		// 设置其他包引用service路径
		StringBuffer iSb = new StringBuffer();
		iSb.append(basePackage).append(".iservice.").append(bean.getModelName()).append(point).append(bean.getUperName()).append(service);
		bean.setImpBeanServcieUrl(iSb.toString());

		// 设置其他包引用service实现的路径
		StringBuffer iiSb = new StringBuffer();
		iiSb.append(basePackage).append(".service.").append(bean.getModelName()).append(point).append(bean.getUperName()).append("ServiceImpl");
		bean.setBeanServiceImplUrl(iiSb.toString());

		// 设置dao小写名称
		bean.setlDaoName(getLowercaseChar(bean.getProxClassName()) + mapper);
	}

	/**
	 * 创建一个空的bean
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void createBean() throws IOException
	{
		String model_path = webPath + "/" + javaFolder + "/" + basePath + "/model/" + bean.getModelName() + "/";

		String path = model_path + "/";
		File filePath = new File(path);
		createFilePath(filePath);

		String fileName = model_path + bean.getUperName() + ".java";
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);

		String beanUrl = basePackage + ".model" + "." + bean.getModelName();
		bean.setBeanUrl(beanUrl);

		// bean模板路径
		String beanVmPath = baseVmPath + "/Model.vm";
		try
		{
			fw.write(createCode(beanVmPath, bean, annotation));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 创建一个dao
	 * 
	 * @throws Exception
	 */
	public void createDao() throws Exception
	{
		String model_path = webPath + "/" + javaFolder + "/" + basePath + "/dao/" + bean.getModelName() + "/";

		String path = model_path + "/";
		File filePath = new File(path);
		createFilePath(filePath);

		String fileName = model_path + bean.getUperName() + "Mapper.java";
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);

		String beanUrl = basePackage + ".dao" + "." + bean.getModelName();
		bean.setBeanUrl(beanUrl);

		String daoVmPath = baseVmPath + "/Dao.vm";
		fw.write(createCode(daoVmPath, bean, annotation));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 创建bean的Service<br>
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanService() throws Exception
	{
		String model_path = webPath + "/" + javaFolder + "/" + basePath + "/iservice/" + bean.getModelName() + "/";

		String path = model_path + "/";
		File filePath = new File(path);
		createFilePath(filePath);

		String fileName = model_path + bean.getUperName() + "Service.java";
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);

		// 设置包路径
		String beanUrl = basePackage + ".iservice" + "." + bean.getModelName();
		bean.setBeanUrl(beanUrl);

		String serviceVmPath = baseVmPath + "/Service.vm";
		fw.write(createCode(serviceVmPath, bean, annotation));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 创建bean的Service的实现<br>
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanServiceImpl() throws Exception
	{
		String model_path = webPath + "/" + javaFolder + "/" + basePath + "/service/" + bean.getModelName() + "/";

		String path = model_path + "/";
		File filePath = new File(path);
		createFilePath(filePath);

		String fileName = model_path + bean.getUperName() + "ServiceImpl.java";
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		// 设置包路径
		String beanUrl = basePackage + ".service" + "." + bean.getModelName();
		bean.setBeanUrl(beanUrl);

		String serviceImplVmPath = baseVmPath + "/ServiceImpl.vm";
		fw.write(createCode(serviceImplVmPath, bean, annotation));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 创建bean的controller<br>
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanController() throws Exception
	{
		String model_path = webPath + "/" + javaFolder + "/" + basePath + "/controller/" + bean.getModelName() + "/";

		String path = model_path + "/";
		File filePath = new File(path);
		createFilePath(filePath);

		String fileName = model_path + bean.getUperName() + "Controller.java";
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		// 设置包路径
		String beanUrl = basePackage + ".controller" + "." + bean.getModelName();
		bean.setBeanUrl(beanUrl);

		String controllerVmPath = baseVmPath + "/Controller.vm";
		fw.write(createCode(controllerVmPath, bean, annotation));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 创建bean的vm文件<br>
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanMapping() throws Exception
	{
		String model_path = webPath + "/" + javaFolder + "/" + basePath + "/mapper/" + bean.getModelName() + "/";

		String path = model_path + "/";
		File filePath = new File(path);
		createFilePath(filePath);

		String fileName = model_path + bean.getUperName() + "Mapper.xml";
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		// 设置包路径
		String beanUrl = basePackage + ".mapper" + "." + bean.getModelName();
		bean.setBeanUrl(beanUrl);

		String mapperVmPath = baseVmPath + "/Mapping.vm";
		fw.write(createCode(mapperVmPath, bean, annotation));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	public void createAddView() throws Exception
	{
		String mapperVmPath = baseVmPath + "/add.vm";
		if (! this.fileIsExists(mapperVmPath))
			return;
		String model_path = webPath + "/" + webFolder + "/" + bean.getModelName() + "/";
		File filePath = new File(model_path);
		createFilePath(filePath);
		String fileName = model_path + bean.getLowerName() + "Add" + ".jsp";
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		
		//是否支持国际化
		if (isHaveI18n.equals("false"))
		{
			mapperVmPath = baseVmPath + "/not_i18n/add.vm";
		}
		fw.write(createCode(mapperVmPath, bean, annotation));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	public void createEditView() throws Exception
	{
		String mapperVmPath = baseVmPath + "/edit.vm";
		if (! this.fileIsExists(mapperVmPath))
			return;
		String model_path = webPath + "/" + webFolder + "/" + bean.getModelName() + "/";
		File filePath = new File(model_path);
		createFilePath(filePath);
		String fileName = model_path + bean.getLowerName() + "Edit" + ".jsp";
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		//是否支持国际化
		if (isHaveI18n.equals("false"))
		{
			mapperVmPath = baseVmPath + "/not_i18n/edit.vm";
		}
		fw.write(createCode(mapperVmPath, bean, annotation));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	public void createShowView() throws Exception
	{
		String mapperVmPath = baseVmPath + "/show.vm";
		if (! this.fileIsExists(mapperVmPath))
			return;
		String model_path = webPath + "/" + webFolder + "/html/" + bean.getModelName() + "/";
		File filePath = new File(model_path);
		createFilePath(filePath);
		String fileName = model_path + "show" + bean.getUperName() + ".html";
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		//是否支持国际化
		if (isHaveI18n.equals("false"))
		{
			mapperVmPath = baseVmPath + "/not_i18n/show.vm";
		}
		fw.write(createCode(mapperVmPath, bean, annotation));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	public void createListView() throws Exception
	{
		//生成的页面文件路径
		String model_path = webPath + "/" + webFolder + "/" + bean.getModelName() + "/";
		File filePath = new File(model_path);
		createFilePath(filePath);
		String fileName = model_path + bean.getLowerName() + "List" + ".jsp";
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		String mapperVmPath = baseVmPath + "/" + this.listHTMLVm;
		//是否支持国际化
		if (isHaveI18n.equals("false"))
		{
			mapperVmPath = baseVmPath + "/not_i18n/list.vm";
		}
		fw.write(createCode(mapperVmPath, bean, annotation));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}
	
	public void createListJSView() throws Exception
	{
		if (StringUtils.isEmpty(this.listJSVm))
			return;
		//生成的页面文件路径
		String model_path = webPath + "/" + webFolder + "/js/" + bean.getModelName() + "/";
		File filePath = new File(model_path);
		createFilePath(filePath);
		String fileName = model_path + bean.getLowerName() + ".js";
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		String mapperVmPath = baseVmPath + "/" + this.listJSVm;
		//是否支持国际化
		if (isHaveI18n.equals("false"))
		{
			mapperVmPath = baseVmPath + "/not_i18n/list.vm";
		}
		fw.write(createCode(mapperVmPath, bean, annotation));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 根据模板生成代码
	 * 
	 * @param fileVMPath
	 *            模板路径
	 * @param bean
	 *            目标bean
	 * @param annotation
	 *            注释
	 * @return
	 * @throws Exception
	 */
	public String createCode(String fileVMPath, Bean bean, Annotation annotation) throws Exception
	{
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("input.encoding", "UTF-8");
		velocityEngine.setProperty("output.encoding", "UTF-8");

		Properties p = new Properties();
		p.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		// 初始化引擎
		velocityEngine.init(p);
		Template template = velocityEngine.getTemplate(fileVMPath);
		//
		// Template template =
		// velocityEngine.getTemplate("src/com/icss/air/forum/vm/demo/demoModel.vm");
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("bean", bean);
		velocityContext.put("annotation", annotation);

		StringWriter stringWriter = new StringWriter();
		template.merge(velocityContext, stringWriter);
		return stringWriter.toString();
	}

	/**
	 * 生成系统架构文件，复制Java及Web文件至目标项目下
	 */
	public void createStructure()
	{
		//javaSource = CodeGreateUtils.class.getClassLoader().getResource("src_source").getPath();
		System.out.println("javaSource:" + javaSource);
		try
		{
			FileUtil.copyDirectiory(javaSource, webPath + File.separator + javaFolder);// 复制Java源文件及配置
			FileUtil.copyDirectiory(webSource, webPath + File.separator + webFolder);// 复制Web文件及配置
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * 创建文件
	 * 
	 * @param file
	 */
	public void createFilePath(File file)
	{
		if (!file.exists())
		{
			System.out.println("创建[" + file.getAbsolutePath() + "]情况：" + file.mkdirs());
		}
		else
		{
			System.out.println("存在目录：" + file.getAbsolutePath());
		}
	}

	/**
	 * 获取路径的最后面字符串<br>
	 * 如：<br>
	 * <code>str = "com.b510.base.bean.User"</code><br>
	 * <code> return "User";<code>
	 * 
	 * @param str
	 * @return
	 */
	public String getLastChar(String str)
	{
		if ((str != null) && (str.length() > 0))
		{
			int dot = str.lastIndexOf('.');
			if ((dot > -1) && (dot < (str.length() - 1)))
			{
				return str.substring(dot + 1);
			}
		}
		return str;
	}

	/**
	 * 把第一个字母变为小写<br>
	 * 如：<br>
	 * <code>str = "UserDao";</code><br>
	 * <code>return "userDao";</code>
	 * 
	 * @param str
	 * @return
	 */
	public String getLowercaseChar(String str)
	{
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	/**
	 * 显示信息
	 * 
	 * @param info
	 */
	public void showInfo(String info)
	{
		System.out.println("创建文件：" + info + "成功！");
	}

	public Bean getBean()
	{
		return bean;
	}

	public void setBean(Bean bean)
	{
		this.bean = bean;
	}

	public Annotation getAnnotation()
	{
		return annotation;
	}

	public void setAnnotation(Annotation annotation)
	{
		this.annotation = annotation;
	}

	public String getWebPath()
	{
		return webPath;
	}

	public void setWebPath(String webPath)
	{
		this.webPath = webPath;
	}

	private boolean fileIsExists(String path){
		File f = new File(path);
		return f.exists();
	}
}
