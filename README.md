JMLab Java 9 Utility Libraries
==============================
## version
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/kr.jmlab/jmlab-utils-java9/badge.svg)](http://search.maven.org/#artifactdetails%7Ckr.jmlab%7Cjmlab-utils-java9%7C0.1.0%7Cjar)

## Prerequisites:
* Java 9 or later

## For Example:
Gradle:
```groovy
compile 'kr.jmlab:jmlab-utils-java9:0.1.0'
```
Maven:
```xml
<dependency>
    <groupId>com.github.jm-lab</groupId>
    <artifactId>jmlab-utils-java9</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Installation
Checkout the source code:

    git clone https://github.com/JM-Lab/utils-java9.git
    cd utils-java9
    git checkout -b 0.1.0 origin/0.1.0
    mvn install

## Useful Utilities With New Features Of Java 9  :
* **Flow Package ([Reactive Programming with JDK 9 Flow API](https://community.oracle.com/docs/DOC-1006738) Utility)**

`JMSubmissionPublisher`
`BulkSubmissionPublisher`
`StringListSubmissionPublisher`
`StringBulkSubmissionPublisher`
`WaitingSubmissionPublisher`
`WaitingBulkSubmissionPublisher`
`BulkWaitingSubmissionPublisher`

`JMTransformProcessor`
`JMConcurrentTransformProcessor`
`JMTransformProcessorBuilder`

`JMSubscriber`
`JMSubscriberBuilder`
* **JMJson**
* **RestfulResourceUpdater**
* **etc.**

###For example :
* **[TestCase](https://github.com/JM-Lab/utils-java9/tree/master/src/test/java/kr/jm/utils)**

## LICENSE
Copyright 2018 Jemin Huh (JM)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.