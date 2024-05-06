package com.example.mlkit.feature.posedetection.presentation.classification;

public class PoseClassificationResult {
    private final Integer reps;
    private final String className;

    public PoseClassificationResult(Integer reps, String className) {
        this.reps = reps;
        this.className = className;
    }

    public Integer getReps() {
        return reps;
    }

    public String getClassName() {
        return className;
    }
}
