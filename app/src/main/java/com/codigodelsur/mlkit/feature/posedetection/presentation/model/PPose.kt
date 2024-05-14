package com.codigodelsur.mlkit.feature.posedetection.presentation.model

import com.google.mlkit.vision.common.PointF3D
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

/**
 * Just a presentation representation of a [com.google.mlkit.vision.pose.Pose] to more easily access
 * its landmark coordinates.
 */
data class PPose(
    val allLandmarks: List<PointF3D>,

    // Head
    val nose: PointF3D,
    val leftEyeInner: PointF3D,
    val leftEye: PointF3D,
    val leftEyeOuter: PointF3D,
    val rightEyeInner: PointF3D,
    val rightEye: PointF3D,
    val rightEyeOuter: PointF3D,
    val leftMouth: PointF3D,
    val rightMouth: PointF3D,
    val leftEar: PointF3D,
    val rightEar: PointF3D,

    // Left arm
    val leftShoulder: PointF3D,
    val leftElbow: PointF3D,
    val leftWrist: PointF3D,
    val leftPinky: PointF3D,
    val leftIndex: PointF3D,
    val leftThumb: PointF3D,

    // Right arm
    val rightShoulder: PointF3D,
    val rightElbow: PointF3D,
    val rightWrist: PointF3D,
    val rightPinky: PointF3D,
    val rightIndex: PointF3D,
    val rightThumb: PointF3D,

    // Left leg
    val leftHip: PointF3D,
    val leftKnee: PointF3D,
    val leftAnkle: PointF3D,
    val leftHeel: PointF3D,
    val leftFootIndex: PointF3D,

    // Right leg
    val rightHip: PointF3D,
    val rightKnee: PointF3D,
    val rightAnkle: PointF3D,
    val rightHeel: PointF3D,
    val rightFootIndex: PointF3D,
)

fun Pose.toPresentation(): PPose? {
    val nose = getPoseLandmark(PoseLandmark.NOSE)
    // Its all or nothing with landmarks so if any of them is null, all of them will and vice-versa.
    return if (nose == null) {
        null
    } else {
        PPose(
            allLandmarks = allPoseLandmarks.map { it.position3D },
            nose = nose.position3D,
            leftEyeInner = landmarkPosition(PoseLandmark.LEFT_EYE_INNER),
            leftEye = landmarkPosition(PoseLandmark.LEFT_EYE),
            leftEyeOuter = landmarkPosition(PoseLandmark.LEFT_EYE_OUTER),
            rightEyeInner = landmarkPosition(PoseLandmark.RIGHT_EYE_INNER),
            rightEye = landmarkPosition(PoseLandmark.RIGHT_EYE),
            rightEyeOuter = landmarkPosition(PoseLandmark.RIGHT_EYE_OUTER),
            leftMouth = landmarkPosition(PoseLandmark.LEFT_MOUTH),
            rightMouth = landmarkPosition(PoseLandmark.RIGHT_MOUTH),
            leftEar = landmarkPosition(PoseLandmark.LEFT_EAR),
            rightEar = landmarkPosition(PoseLandmark.RIGHT_EAR),
            leftShoulder = landmarkPosition(PoseLandmark.LEFT_SHOULDER),
            leftElbow = landmarkPosition(PoseLandmark.LEFT_ELBOW),
            leftWrist = landmarkPosition(PoseLandmark.LEFT_WRIST),
            leftPinky = landmarkPosition(PoseLandmark.LEFT_PINKY),
            leftIndex = landmarkPosition(PoseLandmark.LEFT_INDEX),
            leftThumb = landmarkPosition(PoseLandmark.LEFT_THUMB),
            rightShoulder = landmarkPosition(PoseLandmark.RIGHT_SHOULDER),
            rightElbow = landmarkPosition(PoseLandmark.RIGHT_ELBOW),
            rightWrist = landmarkPosition(PoseLandmark.RIGHT_WRIST),
            rightPinky = landmarkPosition(PoseLandmark.RIGHT_PINKY),
            rightIndex = landmarkPosition(PoseLandmark.RIGHT_INDEX),
            rightThumb = landmarkPosition(PoseLandmark.RIGHT_THUMB),
            leftHip = landmarkPosition(PoseLandmark.LEFT_HIP),
            leftKnee = landmarkPosition(PoseLandmark.LEFT_KNEE),
            leftAnkle = landmarkPosition(PoseLandmark.LEFT_ANKLE),
            leftHeel = landmarkPosition(PoseLandmark.LEFT_HEEL),
            leftFootIndex = landmarkPosition(PoseLandmark.LEFT_FOOT_INDEX),
            rightHip = landmarkPosition(PoseLandmark.RIGHT_HIP),
            rightKnee = landmarkPosition(PoseLandmark.RIGHT_KNEE),
            rightAnkle = landmarkPosition(PoseLandmark.RIGHT_ANKLE),
            rightHeel = landmarkPosition(PoseLandmark.RIGHT_HEEL),
            rightFootIndex = landmarkPosition(PoseLandmark.RIGHT_FOOT_INDEX)
        )
    }
}

private fun Pose.landmarkPosition(landmark: Int) = getPoseLandmark(landmark)!!.position3D
