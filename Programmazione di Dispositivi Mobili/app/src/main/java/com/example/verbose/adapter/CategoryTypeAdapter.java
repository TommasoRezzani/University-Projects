package com.example.verbose.adapter;

import com.example.verbose.model.Category;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class CategoryTypeAdapter extends TypeAdapter<Category> {
    @Override
    public void write(JsonWriter jsonWriter, Category category) throws IOException {
        jsonWriter.jsonValue(String.valueOf(category.getId()));
    }

    @Override
    public Category read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginObject();

        int id = 0;
        String name = null;
        String iconUrl = null;

        while(in.hasNext()){
            String jsonName = in.nextName();
            switch (jsonName){
                case "id":
                    id = in.nextInt();
                    break;
                case "name":
                    name = in.nextString();
                    break;
                case "icon_url":
                    iconUrl = in.nextString();
            }
        }

        in.endObject();

        return new Category(id, name, iconUrl);
    }
}
