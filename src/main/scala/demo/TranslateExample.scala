/*
 Copyright Google Inc. 2018
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package demo

import org.apache.spark.sql.SparkSession

import com.google.cloud.translate.Translate
import com.google.cloud.translate.Translate.TranslateOption
import com.google.cloud.translate.TranslateOptions
import com.google.cloud.translate.Translation

object TranslateExample {
  def main(args: Array[String]): Unit = {
  	if (args.length != 1) {
      System.err.println(
        """
          | Usage: TranslateExample <source>
          |
          |     <source>: Path to the source file to translate
          |
        """.stripMargin)
      System.exit(1)
    }
  	
  	val source = "gs://jphalip-scala-bucket/words.txt" //args(0)
  	
	val spark = SparkSession.builder.appName("Simple Application").getOrCreate()

	val textFile = spark.read.textFile(source)
		
  	val translate = TranslateOptions.getDefaultInstance().getService()
	
	textFile.collect().foreach(line => {
      val translation =
	    translate.translate(
	      line,
	      TranslateOption.sourceLanguage("en"),
	      TranslateOption.targetLanguage("fr"))
	
	    System.out.printf("Text: %s%n", line)
	    System.out.printf("Translation: %s%n", translation.getTranslatedText())
	})
  }
}