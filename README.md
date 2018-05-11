This repository contains a simple demo Spark application that translates words using
Google's Translation API and running on Cloud Dataproc.

Compile the JAR (this may take a few minutes):

```
mvn package
```

Save the project ID in an environment variable for later use:

```
export PROJECT=$(gcloud info --format='value(config.project)')
```

Create a bucket:

```
gsutil mb gs://$PROJECT-bucket
```

Upload `words.txt` to the bucket:

```
gsutil cp words.txt gs://$PROJECT-bucket
```

The file `words.txt` contains the following:

```
cat
dog
fish
```

Enable the Cloud Dataproc and Translation APIs:

```
gcloud services enable dataproc.googleapis.com translate.googleapis.com
```

Create a Cloud Dataproc cluster:

```
gcloud dataproc clusters create demo-cluster \
--zone=us-central1-a \
--scopes=cloud-platform \
--image-version=1.2
```

Submit the Spark job to translate the words to French:

```
gcloud dataproc jobs submit spark \
--cluster demo-cluster \
--jar target/translate-example-1.0.jar \
--properties spark.driver.extraClassPath=translate-example-1.0.jar \
-- fr gs://$PROJECT-bucket words.txt translated-fr
```

Verify that the words have been translated:

```
gsutil cat gs://$PROJECT-bucket/translated-fr/part-*
```

The output is:

```
chat
chien
poisson
```