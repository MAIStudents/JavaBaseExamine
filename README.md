# JavaBaseExamine
Зачёт осень

Каждый создаёт свою ветку от main с названием student/номер_группы_фамилия_первая_буква_имени_номер_билета. Делаем пуш только в конце экзамена. Если пуша в конце экзамена нет, то считается, что задача не сдана.

Билет №18:
1.  Класс ArrayList, LinkedList. Основные реализации. Как работают вставка, удаление, поиск элемента на примере реализаций.
2.  Java Memory Model - основные аспекты. Garbage Collector. Garbage Collector G1. СMS.
3.  Реализуйте программу на Java, клиент-серверное (сокеты) приложение. Клиент отправляет на сервер имя файла и ключевое слово. Сервер получает имя файла и ключевое слово. Сервер читает файл, если он есть и отправляет клиенту все строки, в которых есть ключевое слово. Если файла нет или ключевого слова нет, то сервер возвращает ответ, что файла или ключевого слова нет. Сервер постоянно ждёт новых запросов от клиента, клиент постоянно ожидает ввода данных с консоли. Сервер и клиент завершают свою работу только если клиент отправит на сервер команду exit.


1) **ArrayList**

   Класс ArrayList наследуется от класса AbstractList и реализует следующие интерфейсы: List, RandomAccess, Cloneable, Serializable.

   Вставка, удаление и поиск происходят как в обычном динамическом массиве. При вставке создаётся новый массив, туда копируются все элементы со старого + добавляется новый, при удалении - удаляется необходимый и элементы при необходимости "сдвигаются" влево.
   
Сложность (и в среднем, и в худшем) вставки, удаления и поиска - O(n)

   Примеры кода вставки, удаления, поиска элемента

``` Java
        package org.example;
        
        import java.util.*;
   
        class Main {
   
            public static void main(String args[]) {
   
                ArrayList<String> aList = new ArrayList<String>(); 
                aList.add("1"); // вставка
                aList.add("2");
                aList.add("3");
                aList.add("4");
                aList.add("5");
        
                System.out.println(aList);
        
                aList.remove("1"); // удаление по значению
                aList.remove("3");
        
                System.out.println(aList);
        
                aList.remove(2); // удаление по индексу
        
                System.out.println(aList);

           if (aList.contains("2")) // поиск
            System.out.println("2 exists in the ArrayList");
            }
        }

```

2) **LinkedList**

В LinkedList элементы фактически представляют собой звенья одной цепи. У каждого элемента помимо тех данных, которые он хранит, имеется ссылка на предыдущий и следующий элемент. По этим ссылкам можно переходить от одного элемента к другому. То есть, список двусвязный.

При вставке и удалении элемента просто переставляются ссылки, поэтому их сложность O(1)

Сложность поиска аналогична ArrayList и равна O(n).

В отличие от ArrayList, у LinkedList есть методы для работы с первым и последним элементом списка.

``` Java
package org.example;

import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        String str1 = "1";
        String str2 = "2";
        String str3 = "3";
        String str4 = "4";

        LinkedList<String> lList = new LinkedList<>();
        lList.add(str1); // вставка
        lList.add(str2);
        lList.add(str3);
        lList.add(str4);

        System.out.println(lList);

        lList.remove("1"); // удаление
        lList.remove(2);

        System.out.println(lList);

        String item = "2";

        if (lList.contains(item)) {
            System.out.println("2 is in LinkedList"); // поиск
        }
    }
}

```

