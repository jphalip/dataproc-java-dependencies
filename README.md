
```
mvn package

export PROJECT=$(gcloud info --format='value(config.project)')

gsutil mb gs://$PROJECT-bucket

gsutil cp words.txt gs://$PROJECT-bucket

gcloud services enable dataproc.googleapis.com translate.googleapis.com

gcloud dataproc clusters create demo-cluster \
--zone=us-central1-a \
--scopes=cloud-platform \
--image-version=1.2

gcloud dataproc jobs submit spark \
--cluster demo-cluster \
--jar target/translate-example-1.0.jar \
--properties spark.driver.extraClassPath=translate-example-1.0.jar \
-- gs://$PROJECT-bucket/words.txt
```