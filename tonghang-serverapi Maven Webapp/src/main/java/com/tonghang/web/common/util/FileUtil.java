package com.tonghang.web.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component("fileUtil")
public class FileUtil {
	
	private List<String> adv_path ;
	
	public void gatherAdv_names(File dir){
		File[] fs = dir.listFiles();
	    for(int i=0; i<fs.length; i++){
		    System.out.println(fs[i].getAbsolutePath());
		    if(fs[i].isDirectory()){
		    	gatherAdv_names(fs[i]);
		    }else{
		    	adv_path.add(fs[i].getAbsolutePath().substring(fs[i].getName().lastIndexOf("_")+1,fs[i].getName().lastIndexOf(".")));
		    }
	    }
	}


}
