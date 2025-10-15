package com.smart_task_planner.smart_task_planner.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smart_task_planner.smart_task_planner.Entity.Task;

import okhttp3.*;

@Service
public class PlannerServiceImpl implements PlannerService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.model.name}")
    private String geminiModelName; 

    private static final String GEMINI_URL_TEMPLATE =
            "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";

    @Override
    public List<Task> getTasks(String goal) {
        List<Task> tasks = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        try {
           
            JSONObject textPart = new JSONObject();
            textPart.put("text",
                    "Break down this goal into actionable tasks. "
                            + "Output only a valid JSON array of tasks, with no extra text or markdown formatting. "
                            + "Each task in the array must be a JSON object with these exact keys: "
                            + "'taskName' (string), 'taskDescription' (string), 'startDate' (string, YYYY-MM-DD), "
                            + "'endDate' (string, YYYY-MM-DD), and 'status' (string, 'PENDING' or 'COMPLETED').\n"
                            + "Goal: " + goal+ "start date: "+ LocalDate.now());

            JSONArray partsArray = new JSONArray();
            partsArray.put(textPart);

            JSONObject content = new JSONObject();
            content.put("parts", partsArray);

            JSONArray contentsArray = new JSONArray();
            contentsArray.put(content);

            JSONObject requestJson = new JSONObject();
            requestJson.put("contents", contentsArray);
            
            
            JSONObject generationConfig = new JSONObject();
            generationConfig.put("temperature", 0.2);
            generationConfig.put("topK", 1);
            generationConfig.put("topP", 1);
            generationConfig.put("maxOutputTokens", 8192);
            requestJson.put("generationConfig", generationConfig);


            
            String url = String.format(GEMINI_URL_TEMPLATE, geminiModelName, geminiApiKey);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(requestJson.toString(), MediaType.parse("application/json")))
                    .build();

    
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                 System.err.println("API call failed with code: " + response.code());
                 System.err.println("Response body: " + response.body().string());
                 return tasks; 
            }

            String responseBody = response.body().string();
            System.out.println("Raw Gemini response: " + responseBody);


            
            String jsonArrayStr = extractJsonArrayFromApiResponse(responseBody);
            if (jsonArrayStr == null) {
                System.err.println("No JSON array found in Gemini response!");
                return tasks;
            }
            
            JSONArray jsonArray = new JSONArray(jsonArrayStr);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Task t = new Task();
                t.setTaskName(obj.optString("taskName", "Unnamed Task"));
                t.setTaskDescription(obj.optString("taskDescription", ""));
                t.setGoal(goal);
                t.setStatus(obj.optString("status", "PENDING"));

                try {
                    t.setStartDate(LocalDate.parse(obj.optString("startDate")));
                    t.setEndDate(LocalDate.parse(obj.optString("endDate")));
                } catch (DateTimeParseException e) {
                    System.err.println("Could not parse date, using default. Error: " + e.getMessage());
                    t.setStartDate(LocalDate.now());
                    t.setEndDate(LocalDate.now().plusDays(1));
                }

                tasks.add(t);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    
    private String extractJsonArrayFromApiResponse(String apiResponse) {
        try {
            JSONObject responseJson = new JSONObject(apiResponse);
            String modelOutput = responseJson.getJSONArray("candidates")
                                             .getJSONObject(0)
                                             .getJSONObject("content")
                                             .getJSONArray("parts")
                                             .getJSONObject(0)
                                             .getString("text");
            
            
            int start = modelOutput.indexOf("[");
            int end = modelOutput.lastIndexOf("]");
            if (start != -1 && end != -1 && start < end) {
                return modelOutput.substring(start, end + 1);
            }
        } catch (JSONException e) {
            System.err.println("Error parsing Gemini API response: " + e.getMessage());
            return extractJsonArray(apiResponse);
        }
        return null;
    }


    
    private String extractJsonArray(String text) {
        int start = text.indexOf("[");
        int end = text.lastIndexOf("]");
        if (start != -1 && end != -1 && start < end) {
            return text.substring(start, end + 1);
        }
        return null;
    }
}
