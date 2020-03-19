package com.example.udemylesson60downloadjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Запуск задачи
        DownloadJSONTask task = new DownloadJSONTask();
        // В параметры передать стпоку с сайта
        task.execute();
    }


    // Класс который загружает данные
    private static class DownloadJSONTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();

            // Ctrl + Alt + T - обернуть код
            try {
                url = new URL(strings[0]);
                // открываем соединение
                urlConnection = (HttpURLConnection) url.openConnection();
                // получаем поток ввода
                InputStream inputStream = urlConnection.getInputStream();
                // Создаем reader-ы
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                // Читаем данные
                String line = reader.readLine();
                // Пока строка не будет пустой, добавляем ее в стрингБилдер
                while (line != null) {
                    result.append(line);
                    //читаем следующую строку
                    line = reader.readLine();
                }
                // Возвращаем результат
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // закрываем соединение
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
        // Кроме получения данных, в сетоде onCreate, мы можем вывести их в классе DownloadJSONTask.
        // Для этого нужно переопределить метод onPostExecute
        // Если метод doInBackground не имеет доступа к элементам графического интерфейса,
        // то метод onPostExecute имеет доступ к графическому интерфейсу,
        // в который передается, в качестве параметра, сторока которую возвращает метод doInBackground
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Выводим весь результат одной строкой
            Log.i ("qwerty", s);
        }
    }
}
