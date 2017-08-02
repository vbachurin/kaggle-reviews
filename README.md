# kaggle-reviews

Solution implemented in Scala  

The project resides in `kaggle-reviews/food-reviews` directory   

https://www.kaggle.com/snap/amazon-fine-food-reviews  
The directory containing the Reviews.csv files must reside along with the `food-reviews` project.
Alternatively, the input can be specified using the `--path` and `--file` arguments.

#kaggle-reviews/food-reviews 

This project requires 
- the latest JDK
- the latest SBT build tool
http://www.scala-sbt.org/download.html

RUN COMMAND EXAMPLES:  
`cd kaggle-reviews/food-reviews`  
`sbt "run all"`  
`sbt "run mostActiveUsers"`  
`sbt "run --path ../amazon-fine-foods --file Reviews_small.csv mostActiveUsers mostCommentedFood"`  
`sbt "run --translate"`  

If `--translate` is used, the output is supposed to be saved in `text_fr.csv`.
