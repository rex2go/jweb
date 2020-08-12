package eu.timhuebert.jweb._core.resource;

import eu.timhuebert.jweb._core.response.Response;
import eu.timhuebert.jweb._core.util.FileUtil;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class ResourceManager {

    public Response load(String name) {
        Response response = new Response(Response.StatusCode.OK, new HashMap<String, String>(), new byte[]{});
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("public/" + name);

        if (url == null) return null;

        File file = new File(url.getFile());
        int fileLength = (int) file.length();

        try {
            byte[] fileData = FileUtil.readFileData(file, fileLength);

            // TODO content type
            response.setBody(fileData);
            response.getHeaders().put("Content-Length", String.valueOf(fileLength));

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            // TODO not found
        }
    }
}
