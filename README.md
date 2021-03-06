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

Simplify and shorten the Java print statement.
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

Easy, readable, constructors. A lot of times you find yourself having the same names for fields and constructor parameters, and I actually thought this way was doable when I first learnt Java. However, the line must appear first in the constructor.
```java
public Class(params...) {
    this.* = *;
}
==
public Class(params...) {
    this.param1 = param1;
    this.param2 = param2;
    this.param3 = param3;
    ...
}
```

FString, or Formatted Strings, although found in many other languages, is not present in Java. Well, now it is!
```java
$"The value is {variable}!"
==
"The value is " + (variable) + "!"
```

Simpler Main function.
```java
main() {
==
public static void main(String[] args) {
```

JS Arrays! The java / c# arrays always seemed weird to me, because { and } should signify a block. Well now, you can use the better JS syntax to declare arrays!
```java
Type[] array = [item1, item2, item3...];
==
Type[] array = new Type[]{item1, item2, item2...};
```

Single line conditions. With colons. Looks really neat.
```java
if condition: action;
==
if (condition) action;
```
