1. Download and unzip the code.
2. Modify the parameter of store.nodes.hosts of application.properties(src/main/resources/) file.
   The format is ip:port:position
3. Open a terminal and enter the file path of the code:
	cd /file path/KVStore-main
4. Execute the command:
	mvn clean package
5. Enter the target folder:
	cd target/
6. Execute the following command to start the server. We need to modify the port number.
	java -jar dynamo-1.5.jar --server.port={port}
7. At each server use the above operations to start these servers. 
8. We can open a browser to use these links to do the read, write, and recovery operation.
   Read: http://localhost:8080/get?key=2
   Write: http://localhost:8080/put?key=2&val=a
   Recovery: http://localhost:8083/sendRecover?ip=192.168.50.194&port=8080