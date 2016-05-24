## Apache Kafka and Message Pack Example

#### DESCRIPTION
    # A project that implements Apache Kafka consumer and producer that uses Message Pack java library

#### DEVELOPER SETUP
    PREREQUISITE
        Java 8 SDK Installed
        Maven 3 Installed
        
        Environment Variables SET
        JAVA_HOME=<JAVA_INSTALLATION_DIRECTORY>
        MAVEN_HOME=<MAVEN_INSTALLATION_DIRECTORY>
        
    WINDOWS SETUP
        1.) Download and extract Zookeeper 3.4.8(http://mirror.rise.ph/apache/zookeeper/)
        - Open a Command Prompt
        $ cd C:\zookeeper-3.4.8\conf
        $ ren zoo_sample.cfg zoo.cfg
        
        - Using a text editor(notepad++), edit C:\zookeeper-3.4.8\conf\
        - Replace the line "dataDir=/tmp/zookeeper" to dataDir=C:\zookeeper-3.4.8\data
        - Add System Environment Variable 
            ZOOKEEPER_HOME=C:\zookeeper-3.4.8\
        - Update Path system variable, in the end of the value add
            ;%ZOOKEEPER_HOME%\bin
        - Open a new command prompt
            $ zkserver
            
        2.) Download and extract kafka_2.11-0.9.0.1.tgz(http://kafka.apache.org/downloads.html) to drive C:
        - Open a Command Prompt
        - Using a text editor(e.g. Notedpad++) edit C:\kafka_2.11-0.9.0.1\config\server.properties
        - Replace the line "log.dirs=/tmp/kafka-logs" to "log.dir= C:\kafka_2.11-0.9.0.1\kafka-logs"
        
        - If Zookeeper runs in a different maching edit "zookeeper.connect=localhost:2181, 
          replace localhost to the host IP address and port number. 
          
        - Kafka will run on default port 9092 & connect to zookeeper’s default port which is 2181.
        
        3.) Creating a Kafka topic
        - Open a new command prompt 
          $ cd C:\kafka_2.11-0.9.0.1\bin\windows
          $ kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test-messages

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
        
    USEFUL KAFKA COMMANDS
        List Topics: kafka-topics --list --zookeeper localhost:2181
        Describe Topic: kafka-topics --describe --zookeeper localhost:2181 --topic [Topic Name]
        Read messages from beginning: kafka-console-consumer --zookeeper localhost:2181 --topic [Topic Name] --from-beginning

#### HOW TO RUN
        $ mvn clean install 
        $ java ConsumerTest
        $ java ProducerTest