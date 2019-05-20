package org.opwo.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import groovy.io.FileType

class Metalink extends DefaultTask{
	String fileSet
	
	@TaskAction
	void metalink(){
		
		def dir = new File("${fileSet}")
		dir.eachFileRecurse (FileType.FILES) { file ->
  			println("Name: " + file.name)
			println("Size: " + file.length())
		}
		

		
	}
}