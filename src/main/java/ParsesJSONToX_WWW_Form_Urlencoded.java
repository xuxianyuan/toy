import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * 转json为postman：x-www-form-urlencode格式文件
 */
public class ParsesJSONToX_WWW_Form_Urlencoded {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void parses(String path) {
        File file = new File(path), newFile = crateNewFile(file);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newFile));
            StringBuilder text = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                text.append(line.trim());
            }
            Map map = objectMapper.readValue(text.toString(), Map.class);
            for (Object obj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                if (entry.getValue() instanceof List) {
                    deepParses((List) entry.getValue(), entry.getKey().toString(), bufferedWriter);
                } else if (entry.getValue() instanceof Map) {
                    deepParses((Map) entry.getValue(), entry.getKey().toString(), bufferedWriter);
                } else {
                    bufferedWriter.write(String.valueOf(entry.getKey()) + ":" + String.valueOf(entry.getValue()) + "\r\n");
                }
            }
            bufferedReader.close();
            bufferedWriter.close();
            System.out.println("FINISH!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deepParses(Map map, String prefix, BufferedWriter bufferedWriter) throws IOException {
        for (Object obj : map.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            if (entry.getValue() instanceof List) {
                deepParses((List) entry.getValue(), prefix + "[" + entry.getKey() + "]", bufferedWriter);
            } else if (entry.getValue() instanceof Map) {
                deepParses((Map) entry.getValue(), prefix + "[" + entry.getKey() + "]", bufferedWriter);
            } else {
                bufferedWriter.write(prefix + "[" + String.valueOf(entry.getKey()) + "]:" + String.valueOf(entry.getValue()) + "\r\n");
            }
        }
    }

    private void deepParses(List list, String prefix, BufferedWriter bufferedWriter) throws IOException {
        int count = 0;
        for (Object obj : list) {
            if (obj instanceof List) {
                deepParses((List) obj, prefix + "[" + count + "]", bufferedWriter);
            } else if (obj instanceof Map) {
                deepParses((Map) obj, prefix + "[" + count + "]", bufferedWriter);
            } else {
                bufferedWriter.write(prefix + "[" + count + "]:" + obj + "\r\n");
            }
            count++;
        }
    }

    private File crateNewFile(File oldFile) {
        String fileName = oldFile.getName();
        int indexOfPoint = oldFile.getName().indexOf(".");
        if (indexOfPoint != -1) {
            fileName = fileName.substring(0, indexOfPoint);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(oldFile.getParent()).append("\\").append(fileName).append("_x-www-form-urlencode").append(".md");
        return new File(sb.toString());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ParsesJSONToX_WWW_Form_Urlencoded t = new ParsesJSONToX_WWW_Form_Urlencoded();
        while (scanner.hasNext()) {
            String path = scanner.nextLine().trim();
            t.parses(path);
        }
    }
}
