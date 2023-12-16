package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import java.util.Scanner;

/*
Реализуйте программу на Java, клиент-серверное (сокеты)
приложение. Клиент отправляет на сервер имя файла и
ключевое слово. Сервер получает имя файла и ключевое слово.
Сервер читает файл, если он есть и отправляет клиенту все
строки, в которых есть ключевое слово. Если файла нет или
ключевого слова нет, то сервер возвращает ответ, что файла
или ключевого слова нет. Сервер постоянно ждёт новых запросов
от клиента, клиент постоянно ожидает ввода данных с консоли.
Сервер и клиент завершают свою работу только если клиент
отправит на сервер команду exit.
* */

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public Server() {
    }

    private Socket client;
    private boolean isNotExited = true;

    private File getFileFromResource(String fileName) throws FileNotFoundException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(fileName);

        if (resource == null) {
            throw new FileNotFoundException("Указанный файл: " + fileName + " не найден");
        }
        return new File(resource.toURI());
    }

    public void start() {
        logger.info("Инициализация сервера");
        ServerSocket serverSocket = null;
        try {
            int port = 8843;
            serverSocket = new ServerSocket(port);
            logger.info("Сервер стартовал и ожидает подключение клиента");
            client = serverSocket.accept();
            logger.info("Подключился новый клиент: " + client.toString());

            while (isNotExited) {
                Scanner inputStream = null;
                try {
                    assert client != null;
                    inputStream = new Scanner(client.getInputStream());

                    while (inputStream.hasNext()) {
                        String text = inputStream.nextLine();
                        logger.info("Сообщение от клиента: " + text);
                        if (text.contains("exit")) {
                            isNotExited = false;
                            sendMessage("###exit###");
                            break;
                        }
                        String[] inputString = text.split(" ");
                        if (inputString.length != 2) {
                            logger.info("Неверный ввод от клиента");
                            sendMessage("Неверный ввод: ожидается <имя файла> <ключевое слово>");
                            continue;
                        }
                        String fileName = inputString[0];
                        String keyword = inputString[1];

                        File inputFile;
                        try {
                            inputFile = getFileFromResource(fileName);
                        } catch (FileNotFoundException e) {
                            logger.info("Файл не найден!");
                            sendMessage("Файл не найден");
                            continue;
                        }
                        catch (URISyntaxException e) {
                            logger.error("Ошибка при получении URI для файла");
                            sendMessage("Ошибка при получении URI для файла");
                            continue;
                        }

                        List<String> result = getAllKeywordEntriesFromFile(inputFile, keyword);
                        if (result.isEmpty()) {
                            logger.info("В файле " + fileName + " нет слова " + keyword);
                            sendMessage("В файле " + fileName + " нет слова " + keyword);
                        } else {
                            logger.info("Найдены вхождения " + keyword + " в файле " + fileName);
                            for (String entry : result) {
                                sendMessage(entry);
                            }
                        }
                    }
                } catch (IOException e) {
                    logger.error("Ошибка при работе с клиентом", e);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (client != null) {
                        try {
                            client.close();
                        } catch (IOException ex) {
                            logger.error("Ошибка при закрытии клиента!", ex);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Проблема с сервером", e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    logger.error("Проблема при закрытии сервера", ex);
                }
            }
        }
    }

    private List<String> getAllKeywordEntriesFromFile(File file, String keyword) throws IOException {
        String lowerKeyword = keyword.toLowerCase();
        List<String> result = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String nextLine;
        while ((nextLine = reader.readLine()) != null) {
            if (nextLine.toLowerCase().contains(lowerKeyword)) {
                result.add(nextLine);
            }
        }

        return result;
    }

    private void sendMessage(String message) {
        PrintWriter outputStream;
        try {
            outputStream = new PrintWriter(client.getOutputStream());
            outputStream.println(message);
            outputStream.flush();
        } catch (IOException e) {
            logger.error("Проблема при записи сообщения в поток клиента: " + client.toString(), e);
        }
    }
}
