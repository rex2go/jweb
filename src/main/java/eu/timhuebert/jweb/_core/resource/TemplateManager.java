package eu.timhuebert.jweb._core.resource;

import eu.timhuebert.jweb._core.response.Response;
import eu.timhuebert.jweb._core.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class TemplateManager {

    public Response load(String template) {
        Response response = new Response(Response.StatusCode.OK, new HashMap<String, String>(), new byte[]{});

        if (!template.endsWith(".ph")) template += ".ph";

        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("view/" + template);

        if (url == null) return null;

        File file = new File(url.getFile());
        int fileLength = (int) file.length();
        String contentMimeType = "text/html";

        try {
            byte[] fileData = FileUtil.readFileData(file, fileLength);

            response.setBody(fileData);
            response.getHeaders().put("Content-Type", contentMimeType);
            response.getHeaders().put("Content-Length", String.valueOf(fileLength));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
            // TODO not found
        }

        return response;
    }
}
