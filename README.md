## Apache Kafka and Message Pack Example

#### DESCRIPTION
    # A project that implements Apache Kafka consumer and producer that uses Message Pack java library

#### DEVELOPER SETUP
    MAC OS/X SETUP
    
        1.) Homebrew Installation
        http://brew.sh/
        
        2.) Install Kafka
        $ brew install kafka
       
        3.) Configure brokers (Optional)
        $ cd /usr/local/etc/kafka
        $ cp server.properties server-config-broker1.properties
        $ vi server-config-broker1.properties

        broker.id=1
        port=9092
        log.dirs=/usr/local/var/lib/kafka-logs1
            
        $ cp server.properties server-config-broker2.properties
          
        broker.id=2
        port=9093
        log.dirs=/usr/local/var/lib/kafka-logs2
            
        $ cp server.properties server-config-broker2.properties
            
        broker.id=3
        port=9094
        log.dirs=/usr/local/var/lib/kafka-logs3

        4.) Start Zookeeper
        $ zkserver start

        5.) Start Kafka server
        ** RUNNING SINGLE BROKER ON PORT 9092
        $ kafka-server-start /usr/local/etc/kafka/server.properties
          
        ** RUNNNING MULTIPLE BROKERS. RUN EACH COMMAND ON A SEPARATE TERMINAL.
        $ kafka-server-start /usr/local/etc/kafka/server-config-broker1.properties
        $ kafka-server-start /usr/local/etc/kafka/server-config-broker2.properties
        $ kafka-server-start /usr/local/etc/kafka/server-config-broker3.properties
        
        6.) Create a Kafka topic using "test-messages" as topic name
        $ kafka-topics --create --zookeeper 127.0.0.1:2181 --replication-factor 1 --partitions 1 --topic test-messages

        7.) Create a kafka topic (with replication factor of 3) using "test-messages" as topic name
        $ kafka-topics --create --zookeeper 127.0.0.1:2181 --replication-factor 3 --partitions 1 --topic test-messages

        8.) Testing local Kafka installation
        Open a new terminal and run Kafka's console producer
        $ kafka-console-producer --broker-list 127.0.0.1:9092 --topic test-messages
        OR
        $ kafka-console-producer --broker-list 127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094 --topic test-messages 

        Open a new terminal and run Kafka's console consumer
        $ kafka-console-consumer --zookeeper 127.0.0.1:2181 --topic test-messages --from-beginning
              
        List all topics
        $ kafka-topics --zookeeper 127.0.0.1:2181 --list
              
        Delete a topic
        set delete.topic.enable=true in the server broker properties file(e.g. server.properties)
        $ kafka-topics --zookeeper 127.0.0.1:2181 --delete --topic test-messages

#### HOW TO RUN
        $ mvn clean install 
        $ java ConsumerTest
        $ java ProducerTest