This repository contains a simple demo Spark application that translates words using
Google's Translation API and running on Cloud Dataproc.

1. Compile the JAR (this may take a few minutes):

* Option 1: with Maven
  ```
  cd maven
  mvn package
  ```
* Option 2: with SBT
  ```
  cd sbt
  sbt assembly
  mv target/scala-2.11/translate-example-assembly-1.0.jar target/translate-example-1.0.jar
  ```

2. Record the project ID in an environment variable for later use:
   ```
   export PROJECT=$(gcloud info --format='value(config.project)')
   ```

3. Create a bucket:
   ```
   gsutil mb gs://$PROJECT-bucket
   ```

4. Upload `words.txt` to the bucket:
   ```
   gsutil cp ../words.txt gs://$PROJECT-bucket
   ```
   The file `words.txt` contains the following:
   ```
   cat
   dog
   fish
   ```

5. Enable the Cloud Dataproc and Translation APIs:
   ```
   gcloud services enable dataproc.googleapis.com translate.googleapis.com
   ```

6. Create a Cloud Dataproc cluster:
   ```
   gcloud dataproc clusters create demo-cluster \
   --zone=us-central1-a \
   --scopes=cloud-platform \
   --image-version=1.2
   ```

7. Submit the Spark job to translate the words to French:
   ```
   gcloud dataproc jobs submit spark \
   --cluster demo-cluster \
   --jar target/translate-example-1.0.jar \
   --properties spark.driver.extraClassPath=translate-example-1.0.jar \
   -- fr gs://$PROJECT-bucket words.txt translated-fr
   ```

8. Verify that the words have been translated:
   ```
   gsutil cat gs://$PROJECT-bucket/translated-fr/part-*
   ```
   The output is:
   ```
   chat
   chien
   poisson
   ```
