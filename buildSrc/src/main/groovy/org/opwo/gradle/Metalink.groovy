package org.opwo.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import groovy.io.FileType

import java.security.MessageDigest
import java.security.DigestInputStream

import groovy.xml.MarkupBuilder

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
			xmlBuilder.published("Data")
			
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