# Low Level System Design - Multi-level Cache System 

### Video Explanation
[https://www.youtube.com/playlist?list=PL564gOx0bCLp8Mqv3nlE8QP8UaX0hKkEA](https://www.youtube.com/playlist?list=PL564gOx0bCLp8Mqv3nlE8QP8UaX0hKkEA)

SOLID principles followed
Design Patterns User:
1 Chain of Responsibility: l1->l2->l3(recursion) Decoupling
2 Template Patter: interface are used 
3 Composition: NullEffectCache: last level 

Immutability as in lastcount
Generics <key, value>
@NOT null in public methods


### Problem Statement
[Check here](problem-statement.md)

### Project Requirements

* JDK 1.8
* Maven
* For Unit Tests:  
  * Junit 5
  * Mockito

### Compiling/Building and running the unit tests
mvn clean install


