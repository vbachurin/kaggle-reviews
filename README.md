# kaggle-reviews

Solution implemented in Scala  
Took me ~8 hours  
The challenging part was picking the most suitable tool for task 4  
The current version is quite ugly - not all requirements from the assignment are met.  
The solution can be improved upon request.  

I didn't have time to implement a single entry point to launch each task.   
Solutions for tasks 1, 2 and 3 reside in kaggle-reviews/food-reviews directory   
Solution for task 4 is inside kaggle-reviews/translate-reviews  

https://www.kaggle.com/snap/amazon-fine-food-reviews  
The directory containing the Reviews.csv files must reside along with project - in the repository root (I didn't commit it).

`ls kaggle-reviews/`  
amazon-fine-foods   food-reviews   translate-reviews


#kaggle-reviews/food-reviews 

This project requires 
- the latest JDK
- the latest SBT build tool
http://www.scala-sbt.org/download.html
- the latest Apache Spark 
http://spark.apache.org/downloads.html
- the latest Apache Hadoop
https://hadoop.apache.org/releases.html
OR
'winutils' in case of Windows
https://github.com/steveloughran/winutils
- SPARK_HOME and HADOOP_HOME env variables correctly specified

RUN:  
`cd kaggle-reviews/food-reviews`  
`sbt package`  
`spark-submit --class "com.foodreviews.spark.FR1000MostActiveUsers" target/scala-2.11/food-reviews_2.11-1.0.jar`  
`spark-submit --class "com.foodreviews.spark.FR1000MostCommentedFood" target/scala-2.11/food-reviews_2.11-1.0.jar`  
`spark-submit --class "com.foodreviews.spark.FR1000MostUsedWords" target/scala-2.11/food-reviews_2.11-1.0.jar`  


#kaggle-reviews/translate-reviews 

This project requires 
- the latest JDK
- the latest SBT build tool
http://www.scala-sbt.org/download.html

RUN:  
`cd kaggle-reviews/food-reviews`  
`sbt gatling:testOnly`  

The translated reviews are supposed to be saved in   
`kaggle-reviews/text_fr.csv`
