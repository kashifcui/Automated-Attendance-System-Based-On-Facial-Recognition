package com.atharvakale.facerecognition.moduleclasses;

public class Utils {
    public static String convertToString(float[][] array) {
        StringBuilder sb = new StringBuilder();
        for (float[] row : array) {
            for (float value : row) {
                sb.append(value).append(",");
            }
            sb.deleteCharAt(sb.length() - 1); // Remove the trailing comma for each row
            sb.append(";"); // Use a delimiter to separate rows
        }
        sb.deleteCharAt(sb.length() - 1); // Remove the trailing delimiter
        return sb.toString();
    }

    public static float[][] convertToArray(String str) {

        String[] rows = str.split(";");
        float[][] array = new float[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] values = rows[i].split(",");
            float[] row = new float[values.length];
            for (int j = 0; j < values.length; j++) {
                row[j] = Float.parseFloat(values[j]);
            }
            array[i] = row;
        }
        return array;


    }
}
