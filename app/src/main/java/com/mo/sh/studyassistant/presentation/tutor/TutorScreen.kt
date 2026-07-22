package com.mo.sh.studyassistant.presentation.tutor

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mo.sh.studyassistant.R
import com.mo.sh.studyassistant.domain.model.MessageSection
import com.mo.sh.studyassistant.presentation.MainViewModel
import com.mo.sh.studyassistant.presentation.common_components.AttachmentType
import com.mo.sh.studyassistant.presentation.common_components.BaseChatSurface

@Composable
fun TutorScreen(
    sharedImageUri: Uri?,
    sharedText: String?,
    viewModel: MainViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.updateSection(MessageSection.Tutor)
    }
    var text by rememberSaveable {
        mutableStateOf(sharedText ?: "")
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val chats by viewModel.chats.collectAsState()
    val networkState by viewModel.loadingState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        sharedImageUri?.let {
            imageUri = it
        }
    }

    BaseChatSurface(
        textFieldText = text,
        title = stringResource(R.string.personal_tutor_title),
        visibleAttachmentItems = listOf(
            AttachmentType.Image,
        ),
        selectedImageUri = imageUri,
        chats = chats,
        loadingState = networkState,
        onWritePdf = { message, uri ->
            viewModel.writePdfFile(
                message,
                uri
            ) {
                Toast.makeText(
                    context,
                    if (it) {
                        context.getString(R.string.pdf_saved)
                    } else context.getString(R.string.error_creating_pdf),
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        onReset = {
            viewModel.resetChat()
        },
        onDeleteAll = {
            viewModel.clearAllChats()
        },
        onImageSelected = {
            imageUri = it
        },
        onPdfSelected = {},
        onTextChanged = { text = it },
        textFieldHint = stringResource(R.string.tutor_chat_hint),
        onSubmit = { message ->
            if (message.isBlank() && imageUri == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.provide_content),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Find the latest chat that isn't done to maintain context
                val activeChat = chats.firstOrNull { chat -> !chat.chat.done }
                viewModel.sendMessage(
                    message = message,
                    imageUri = imageUri,
                    chat = activeChat
                )
            }
            imageUri = null
            text = ""
        }
    )
}
