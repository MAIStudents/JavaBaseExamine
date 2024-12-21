Билет №16:
1.  Сетевое взаимодействие клиент-сервер в Java. Основные классы и методы для работы с сетью.
2.  Java Memory Model - основные аспекты. Garbage Collector. Garbage Collector G1. СMS.
3.  Реализуйте программу на Java, которая считает медиану чисел. Реализовать клиент и сервер (сокеты). Клиент отправляет набор чисел, сервер принимает запрос от клиента, высчитывает медиану и отправляет обратно запросившему клиенту и дальше ждет новых запросов от клиента.,


1. В модели Клиент - Сервер можно выделить три класса:

- Сервер
- Клиент
- Обработчик клиентов

Основные классы и методы:
Socket:
Socket(String имя_хоста, int порт) throws UnknownHostException, IOException
Socket(InetAddress IP-адрес, int порт) throws UnknownHostException.

Методы Socket:

InetAddress getInetAddress() – возвращает объект содержащий данные о сокете. В случае если сокет не подключен – null

int getPort() - возвращает порт по которому происходит соединение с сервером

int getLocalPort() – возвращает порт к которому привязан сокет. Дело в том, что «общаться» клиент и сервер могут по одному порту, а порты, к которым они привязаны – могут быть совершенно другие.

boolean isConnected() – возвращает true, если соединение установлено.

void connect(SocketAddress адрес) – указывает новое соединение.

boolean isClosed() – возвращает true, если сокет закрыт.

boolean isBound() - возвращает true, если сокет действительно привязан к адресу
Класс Socket реализует интерфейс AutoCloseable, поэтому его можно использовать в конструкцииtry-with-resources. Тем не менее закрыть сокет также можно классическим образом, с помощью close().

ServerSocket:

ServerSocket()throws IOExceptionServerSocket(int порт)throws IOExceptionServerSocket(int порт,int максимум_подключений)throws IOExceptionServerSocket(int порт,int максимум_подключений, InetAddress локальный_адрес)throws IOException

Доступ к потоку:

InputStream getInputStream()
OutputStream getOutputStream()
BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


2.Java Memory Model

Разделение памяти. Память в Java можно условно разделить на следующие разделы:

- Heap
- Non-Heap
- Stack

Heap- это основной сегмент памяти, где хранятся объекты. Он делится на два подсегмента: Old Generation и New Generation. New Generation делится на подсегменты: Eden и два сегмента Survivor. При запуске Java приложения JVM загружает необходимые классы в кучу.

Non-heap подразделяется на: Permanent Generation и Code Cache. Permanent Generation содержит набор метаинформации о классах, которая может использоваться.

Stack- это память, своего рода "оперативная память метода", работающая по схеме LIFO. Когда вызывается метод, то для него в памяти стека создаётся новый блок, называемый фрэймом. Он содержит: примитивные типы, ссылки на другие объекты.

Garbage Collector отвечает за автоматическое управление памятью в Java, удаляя неиспользуемые объекты для освобождения ресурсов. Основные принципы:

- Отслеживание доступности объектов:
GC анализирует дерево ссылок от корневых объектов (GC Roots).
Объекты, недоступные из корневых объектов, помечаются как кандидаты на удаление.

- Типы корневых объектов (GC Roots):
Ссылки из локальных переменных стека, ссылки из статических переменных классов, ссылки из регистров процессора.

- Основные этапы работы GC:
Mark: поиск объектов, доступных из GC Roots.
Sweep: удаление недоступных объектов.
Compact: дефрагментация памяти для устранения фрагментации.


Основные реализации Garbage Collector.

- Serial GC:
- - Однопоточный сборщик мусора.
- - Используется для небольших приложений.
- - Преимущество: простота.
- - Недостаток: паузы для всех потоков

- Parallel GC:
- - Многопоточная версия Serial GC.
- - Использует несколько потоков для этапов Mark, Sweep, Compact.
- - Подходит для приложений, где важно высокое пропускание.

- CMS:
- - Минимизирует паузы.
- - Недостаток: фрагментация памяти.
- G1 GC:
- - Делит память на регионы фиксированного размера.
- - Преимущество: предсказуемое время пауз.