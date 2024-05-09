package com.codigodelsur.mlkit.core.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.codigodelsur.mlkit.R

sealed class PMLKitFeature(
    @DrawableRes open val imageRes: Int,
    @StringRes open val titleRes: Int,
    @StringRes open val descriptionRes: Int,
) {
    data object DocumentScanner : PMLKitFeature(
        imageRes = R.drawable.card_document_scanner,
        titleRes = R.string.feature_document_scanner_title,
        descriptionRes = R.string.feature_document_scanner_description
    )

    data object TextRecognition : PMLKitFeature(
        imageRes = R.drawable.card_text_recognition,
        titleRes = R.string.feature_text_recognition_title,
        descriptionRes = R.string.feature_text_recognition_description
    )

    data object FaceDetection : PMLKitFeature(
        imageRes = R.drawable.card_face_detection,
        titleRes = R.string.feature_face_detection_title,
        descriptionRes = R.string.feature_face_detection_description
    )

    data object ObjectDetection : PMLKitFeature(
        imageRes = R.drawable.card_object_detection,
        titleRes = R.string.feature_object_detection_title,
        descriptionRes = R.string.feature_object_detection_description
    )

    data object BarcodeScanner : PMLKitFeature(
        imageRes = R.drawable.card_barcode_scanning,
        titleRes = R.string.feature_barcode_scanner_title,
        descriptionRes = R.string.feature_barcode_scanner_description
    )

    data object PoseDetection : PMLKitFeature(
        imageRes = R.drawable.card_pose_detection,
        titleRes = R.string.feature_pose_detection_title,
        descriptionRes = R.string.feature_pose_detection_description
    )

    data object SelfieSegmentation : PMLKitFeature(
        imageRes = R.drawable.card_selfie_segmentation,
        titleRes = R.string.feature_selfie_segmentation_title,
        descriptionRes = R.string.feature_selfie_segmentation_description
    )

    data object Translation: PMLKitFeature(
        imageRes = R.drawable.card_translation,
        titleRes = R.string.feature_translation_title,
        descriptionRes = R.string.feature_translation_description
    )

    data object SmartReply: PMLKitFeature(
        imageRes = R.drawable.card_smart_reply,
        titleRes = R.string.feature_smart_reply_title,
        descriptionRes = R.string.feature_smart_reply_description
    )

    companion object {
        val all = listOf(
            TextRecognition,
            BarcodeScanner,
            FaceDetection,
            SelfieSegmentation,
            ObjectDetection,
            PoseDetection,
            DocumentScanner,
            Translation,
            SmartReply
        )
    }
}