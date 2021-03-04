# SugarJava
for over, array@index, and more sugar syntax!

Loop over an array (or Iteritable) with an index simply.
```java
for (index over array)
==
for (int index = 0; index < array.length; index++)
```

Access an array member simply, with @ meaning (and read as) "at".
```java
array@index
==
array[index]
```

Simplify and shorten the java print statement.
```java
print x;
==
System.out.println(x);
```

Extend the +=, -=, *=, /=, and %= syntax to other, custom or language given, methods.
```java
value = .someMethod();
==
value = value.someMethod();
```
