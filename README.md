# Task planning algorithm for a datacenter

## Project goals
* Implement a task dispatcher that receives the tasks and then sends them to a host based on the rules specified by one of the four implemented algorithms. The hosts simulate the execution of a task and also allow a current task to be paused and then resumed if a new task with higher priority arrives.

## Algorithm details
The "MyDispatcher" class sends the tasks to a host based on the rule with which it was initialized:

``` java
    Round Robin(RR)
```
* Tasks are assigned one by one to each host until the last host is selected, then it starts again from the first one and so on.

``` java
    Shortest Queue(SQ)
```
* Task is assigned to the host with the shortest queue.

``` java
    Size Interval Task Assignment(SITA)
```
* Task is given to a host based on it's size interval (short, medium, long). In this scenario we only have 3 hosts, one for each size interval.

``` java
    Least Work Left(LWL)
```
* Task is given to the host which has the least amount of work left (including the task that is currently processed). If two hosts have the same amount of work left, we choose the one with the smaller ID.

## Notes
* The desired scheduling algorithm can be chosen when instantiating the "My Dispatcher" class.
* This implementation was made using the Amazon Corretto 19 JDK.
* In the checker folder, there are some tests that can be used to check the implementation.

