## Java EE 7 Tutorial: Java Message Service (JMS) API

[Java Platform, Enterprise Edition: The Java EE Tutorial. Oracle](https://docs.oracle.com/javaee/7/tutorial/) - web version

[The Java EE Tutorial. Oracle](https://docs.oracle.com/javaee/7/JEETT.pdf) - pdf
___
#### - **websimplemessage-plus-mdb**

This application demonstrates how to send messages from an enterprise bean (in this case, a session bean) rather than from an application client

It uses sending and receiving Facelets pages as well as corresponding backing beans. When a user enters a message in the text field of the sending page and clicks a button, the backing bean for the page sends the message to a queue and displays it on the page. When the user goes to the receiving page and clicks another button, the backing bean for that page receives the message synchronously and displays it

<p align="center">
    <img src="https://docs.oracle.com/javaee/7/tutorial/img/jeett_dt_037.png">
</p>

___
This is a modified project [websimplemessage](https://github.com/lytves/websimplemessage) with following modifications:
+  In the page that sends the message, now specify in a text the name of the sender (which will be sent as a message property)
+  The message passing is asynchronous and the reception by a new message-driven bean (*ReceiverMDBBean*)
+  A message-by-subscription step is used where the receiving bean will only accept messages coming from a particular sender
+  The processing of the message saves the data in a new EJB that simulates a Storage of data (*EntityBean*)
+  The receiving page (its Managed Bean - *ReceiverBean*) queries this new EJB to get all messages exchanged (only those received from the filtered sender) and displays them on the screen.
___
1. To activate the message flow in a aplication server WildFly it is necessary to start a full instance of the application server with the following command:
   ><HOME_WILDFLY/bin>$ ./standalone.sh -c standalone-full.xml

2. *JMS Connection Factories* and *JMS Destinations* available on the application server can be viewed, created and modified through the administration interface WildFly (Configuration > Subsystems > Messaging - ActiveMQ > default > Queues / Topics).

   For a correct execution it is necessary to create a new Connection Factory with name **"java:/jms/factoriaConexiones"** and connector type "in-vm" in your aplication server from the administration console.

   For a correct execution it is necessary to create a new target topic **"java:/jms/topic/webappTopic"** in your aplication server from the administration console.

3. Deploy your aplication on a aplication server and in a web browser enter the following URL:
http://localhost:8080/websimplemessage-plus-mdb/