- Create your own queue that will hold messages in form of JSON. Standard library queues were not allowed.
- There was one publisher that can generate messages.
- There are multiple subscribers that will listen messages satisfying a particular regex.
- Subscribers should not be tightly coupled to system and can be added or removed at runtime.
- When a subscriber is added to the system, it registers callback function along with it. And this callback function will be invoked in case some message arrives.
- There can be dependency relationship among subscribers ie if there are two subscribers say A and B and A knows that B has to listen and process first, then only A can listen and process. 
- There was many-to-many dependency relationship among subscribers.
- There must a retry mechanism for handling error cases when some exception occurs in listening/ processing messages, that must be retried.



        - sub1
pub1    - sub2
        - sub3


queue




