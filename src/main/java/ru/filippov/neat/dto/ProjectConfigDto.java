package ru.filippov.neat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectConfigDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NormalizedDataDto {
        @NotNull
        Double minRange;

        @NotNull
        Double maxRange;

        @NotNull
        @NotEmpty
        List<List<Double>> data;

        @NotNull
        @NotEmpty
        List<Double> mins;

        @NotNull
        @NotEmpty
        List<Double> maxs;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataIndexesDto {
        @NotNull
        Integer trainEndIndex;

        @NotNull
        Integer testEndIndex;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectedColumnsDto {
        @NotNull
        Integer inputs;

        @NotNull
        Integer outputs;

        @NotNull
        @NotEmpty
        List<HashMap<String, String>> headers;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PredictionParamsDto {
        @NotNull
        Short windowSize;

        @NotNull
        Short predictionPeriod;
    }

    @NotNull
    private NormalizedDataDto normalizedData;

    @NotNull
    private List<Map<String, Object>> settings;

    @NotNull
    private DataIndexesDto dataIndexes;

    @NotNull
    private SelectedColumnsDto selectedColumns;

    @NotNull
    private PredictionParamsDto predictionParams;




}
