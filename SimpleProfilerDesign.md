### What is  Simple Profiler ###
Simple Profiler is a java profiler tool which is used to profile run time java application.

  * Simple Profiler aims to provide an easy way to analyze exectued methods  based on method run.
    1. for one example, simple profiler can be used to find :  In one certain running java application, from time A to time B, log all methods run, and analyze which methods takes more time than others.


### 3 Parts ###
Simple Profiler contains 3 part.

#### Compile time instrumentor ####
  1. compile time weaver is used to instrument java class bytecode file under priofile. After instrument. Certain hook API is added into bytecode which invokes client part API.
  1. Compile time instrumentor does its isntrument work BEFORE java run time. The instrumentor should be able to instrument class file and jar file.

#### Client part ####
  1. client part stays with in the process where the java application is profiled.
  1. client part provide a single public API which can be invoked by the java program under profile.
  1. Java program under profile must be able to access client part API.
    1. `SimpleProfier` uses java extension class loader mechanism to let java program under profile be able to access the client part API.
    1. which means we put the client part class file under certain dir in JRE (/bin/classes)
  1. the single public API will be invoked and send the information to the server part
  1. client part is also capable of recieve message from server part. Such message is certain configuration or command send from server side. (for example to start/stop logging, to start log method start and end, etc...)



### Client Part Protocol ###
Client part can send the message to Server in the following format:
  * `"#<pid>.<class>.<method>.<methodPara>_<time>"`
    1. pid:  process id of the method being executed
    1. class: full class name
    1. method: method name
    1. methodPara: method para string
      1. for example , "{}" means no para
      1. "{int}" one para int.
    1. time: method execution time in milliseconds.

Client part can also receive message from server side,these message are configurations and commands. For example, client part may recieve commands from server part to start/stop send message.


> #### Server part ####
    1. server part is a stand alone java process which receive messages from client part and present these message in certain format after analyze.
    1. server part can also send command or configuration message to client side.
    1. server part should contain GUI for user to easily input their detail requirement.
    1. In `SimpleProfiler`, Server part chould be an eclipse UI plug-in, or a standalone RCP, or just a standalone java GUI application.


