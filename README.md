### Sparklify Distributed Triple Store

### Dev Setup

Install the development environment as described in this [gitrepo](https://github.com/earthquakesan/hdfs-spark-hive-dev-setup).
To be able to run this application Hadoop, Spark, Hive Metastore and Hive Server should be running.
Assuming that you have installed environment into ~/Workspace/hadoop-spark-hive, create a src/ folder:
```
cd ~/Workspace/hadoop-spark-hive
mkdir src && cd src
```

and clone this repo into src folder:
```
git clone https://github.com/earthquakesan/sparklify
cd sparklify
``` 

Now you can configure the application:
```
make
```

Convert the sample file and load it into Hive server:
```
make run
```

And run Sparqlify to connect to Hive and convert tables to RDF triples (you first might need to ```cd ../../``` and run ```make activate && source activate``` to have hadoop command on your PATH):
```
make sparqlify
```
