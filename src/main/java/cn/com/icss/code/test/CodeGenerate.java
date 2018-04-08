package cn.com.icss.code.test;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cn.com.icss.code.model.Annotation;
import cn.com.icss.code.model.Bean;
import cn.com.icss.code.util.CodeGreateUtils;
import cn.com.icss.code.util.DbUtils;

public class CodeGenerate {
	
	public void showWindow(){
		final JFrame frame = new JFrame();
		JPanel panel = new JPanel(); 
		panel.setLayout(new GridLayout(7,2));
		panel.add(new JLabel("数据库表名："));
		final JTextField tableName = new JTextField("");
		panel.add(tableName);
		panel.add(new JLabel("模块名称(包名)："));
		final JTextField moduleName = new JTextField("");
		panel.add(moduleName);
		panel.add(new JLabel("实体名称(类名)："));
		final JTextField proxClassName = new JTextField("");
		panel.add(proxClassName);
		panel.add(new JLabel("实体中文名称："));
		final JTextField proxClassZhName = new JTextField("");
		panel.add(proxClassZhName);
//		panel.add(new JLabel("工程名称："));
//		final JTextField projectName = new JTextField("");
//		panel.add(projectName);
		panel.add(new JLabel("作者名称："));
		final JTextField authorName = new JTextField("muguoping");
		panel.add(authorName);
		Button generate = new Button("gen code");
		ActionListener a1=new ActionListener(){   
			public void actionPerformed(ActionEvent e){    
				generateCode(tableName.getText(),moduleName.getText(),proxClassName.getText(),proxClassZhName.getText(),authorName.getText());
			}   
		};   
		generate.addActionListener(a1);  
		panel.add(generate);
		Button autoProject = new Button("gen sys structure");
		autoProject.addActionListener(new ActionListener(){   
			public void actionPerformed(ActionEvent e){    
				generateStructure();
			}   
		});
		panel.add(autoProject);
		
		frame.add(panel);   
		frame.setSize(400,300);   
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        frame.setLocation( (screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);   
		//添加窗口监听事件
		frame.addWindowListener(new WindowAdapter(){     
			public void windowClosing(WindowEvent e){            
				System.exit(0);            
			}      
		});
	}
	

	public void generateStructure() {
		CodeGreateUtils cgu = new CodeGreateUtils();
		cgu.createStructure();
	}


	public void generateCode(String tableName,String moduleName,String proxClassName,String proxClassZhName,String authorName){
		CodeGreateUtils cgu = new CodeGreateUtils();
    	Bean bean = new Bean();
    	DbUtils dbUtils = new DbUtils();
    	dbUtils.setTableName(tableName);
    	dbUtils.setColumn();
    	//数据库，生成bean
    	bean.setLcolumnList(dbUtils.getLcolumnList());
    	bean.setUcolumnList(dbUtils.getUcolumnList());
    	bean.setTbColumnList(dbUtils.getTbColumnList());
    	// 加入注释.
    	bean.setCommentList(dbUtils.getCommentList());
    	System.out.print("\n\n"+dbUtils.getCommentList()+"\n\n");
    	bean.setTypeList(dbUtils.getTypeList());
    	bean.setJdbcList(dbUtils.getJdbcList());
    	bean.setWebAddPageFields(dbUtils.getWebAddPageFields());
    	bean.setWebUpdatePageFields(dbUtils.getWebUpdatePageFields());
    	bean.setWebListPageFields(dbUtils.getWebListPageFields());
    	bean.setWebQueryConditionFields(dbUtils.getWebQueryConditionFields());
    	bean.setTableName(tableName);
    	bean.setProxClassName(proxClassName);
    	bean.setProxClassZhName(proxClassZhName);
    	bean.setModelName(moduleName);
    	cgu.setBean(bean);
    	Annotation an = new Annotation();
    	an.setAuthorName(authorName);
    	cgu.setAnnotation(an);
    	cgu.init();
    	try {
    		
    		cgu.createBean();
    		cgu.createBeanController();
    		cgu.createBeanService();
			cgu.createBeanServiceImpl();
    		cgu.createDao();
    		cgu.createBeanMapping();
    		cgu.createAddView();
    		cgu.createEditView();
    		//cgu.createShowView();
    		cgu.createListView();
    		cgu.createListJSView();
    		System.out.println("success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error");
		}
	}

	public static void main(String[] args) {
    	CodeGenerate cg = new CodeGenerate();
    	cg.showWindow();
	}
}
