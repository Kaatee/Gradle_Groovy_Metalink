package org.opwo.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import groovy.io.FileType
import java.security.MessageDigest
import java.security.DigestInputStream
import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat

class Metalink extends DefaultTask{
	String fileSet

	def generateMD5(File file) {
		try{
   			file.withInputStream {
      			new DigestInputStream(it, MessageDigest.getInstance('MD5')).withStream {
         			it.eachByte {}
         			it.messageDigest.digest().encodeHex() as String
      			}
   			}
		}catch(Exception e){
			return "Cannot prepare md5 for this file!"
		}
	}


	@TaskAction
	void metalink(){
		
		def dir = new File("${fileSet}")

		def fileWriter = new FileWriter("zad-opwo.xml")
		def xmlBuilder = new MarkupBuilder(fileWriter)
		xmlBuilder.mkp.xmlDeclaration(version: "1.0", encoding: "utf-8")

		xmlBuilder.metalink(xmls:"urn:ietf:params:xml:ns:metalink") {
			xmlBuilder.published(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()))
			
			dir.eachFileRecurse (FileType.FILES) { file ->
				xmlBuilder.file("name": file.name){
					xmlBuilder.size(file.length())
					xmlBuilder.hash("type": "md5", generateMD5(file))
				}			
			}
		}
		fileWriter << xmlBuilder.toString()
	}
}

//2019-04-29T10:00:00Z