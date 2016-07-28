default:
	cd lib; bunzip2 *

run:
	sbt package
	spark-submit --jars $(shell pwd)/lib/dissect-rdf-spark-0.0.1.jar --class org.aksw.sparklify.Sparklify target/scala-2.11/sparklify_2.11-0.0.1.jar /home/ivan/Workspace/hadoop-spark-hive/src/sparklify/resources/100_infobox_properties_commons.nt

sparqlify:
	sparqlify -m $(shell pwd)/resources/sml_mappings/spo_splt_simple.sml -c $(shell pwd)/lib/hive-jdbc-2.1.0-standalone.jar -j jdbc:hive2://localhost:10000
