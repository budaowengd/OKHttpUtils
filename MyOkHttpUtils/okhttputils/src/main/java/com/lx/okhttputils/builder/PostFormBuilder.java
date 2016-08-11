package com.lx.okhttputils.builder;


import com.lx.okhttputils.request.PostFormRequest;
import com.lx.okhttputils.request.RequestCall;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luoXiong on 16/07/26.
 */
public class PostFormBuilder extends OkHttpRequestBuilder<PostFormBuilder> {
    private List<FileInput> files = new ArrayList<>();

    public PostFormBuilder(String url){
        super(url);
    }

    @Override
    public RequestCall build() {
        PostFormRequest request = new PostFormRequest(url, tag, params, headers, files, id);
        RequestCall call = request.build();
        return call;
    }

    public PostFormBuilder files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(key, filename, files.get(filename)));
        }
        return this;
    }

    public PostFormBuilder files(String name, String filename, File file) {
        files.add(new FileInput(name, filename, file));
        return this;
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }


    public PostFormBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public PostFormBuilder params(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }


}
