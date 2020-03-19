package com.example.udemylesson60downloadjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");
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
            Log.i("qwerty", s);
            // Преобразование строки в Json объект
            try {
                JSONObject jsonObject = new JSONObject(s);

                // получить строку
                String name = jsonObject.getString("name");
                Log.i("qwerty", name);

                // В JSON хранятся другие JSON-ы. Получаем строки из JSON внутри JSON-а
                JSONObject main = jsonObject.getJSONObject("main");
                String temp = main.getString("temp");
                Log.i("qwerty", temp);
                String pressure = main.getString("pressure");
                Log.i("qwerty", pressure);

                // Данные указанные в квадратных скобках - являются массивами
                // получаем массив, затем 0-ой элемент и из него получить данные


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

/*
{"coord":{"lon":-0.13,"lat":51.51},"weather":[{"id":300,"main":"Drizzle","description":"light intensity drizzle","icon":"09d"}],"base":"stations","main":{"temp":280.32,"pressure":1012,"humidity":81,"temp_min":279.15,"temp_max":281.15},"visibility":10000,"wind":{"speed":4.1,"deg":80},"clouds":{"all":90},"dt":1485789600,"sys":{"type":1,"id":5091,"message":0.0103,"country":"GB","sunrise":1485762037,"sunset":1485794875},"id":2643743,"name":"London","cod":200}
*/
